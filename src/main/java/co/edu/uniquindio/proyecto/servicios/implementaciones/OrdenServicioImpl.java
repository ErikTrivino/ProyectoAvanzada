package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import co.edu.uniquindio.proyecto.modelo.vo.Pago;
import co.edu.uniquindio.proyecto.repositorios.OrdenRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.OrdenServicio;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenServicioImpl  implements OrdenServicio {

    private final OrdenRepo ordenRepo;
    private final EventoServicio eventoServicio;

    @Override
    public String crearOrden(CrearOrdenDTO crearOrdenDTO) throws Exception {
        Orden nuevaOrden = new Orden();
        nuevaOrden.setIdCliente(crearOrdenDTO.idCliente());
        nuevaOrden.setFecha(LocalDateTime.now());
        nuevaOrden.setCodigoPasarela(crearOrdenDTO.codigoPasarela());
        nuevaOrden.setItems(crearOrdenDTO.items());
        nuevaOrden.setTotal(crearOrdenDTO.total());

        ordenRepo.save(nuevaOrden);
        return "La orden ha sido creada con éxito";
    }

    @Override
    public String actualizarOrden(EditarOrdenDTO editarOrdenDTO) throws Exception {
        Orden orden = obtenerOrden(editarOrdenDTO.id());

        orden.setItems(editarOrdenDTO.items());
        orden.setTotal(editarOrdenDTO.total());

        ordenRepo.save(orden);
        return "La orden ha sido actualizada con éxito.";
    }

    @Override
    public String eliminarOrden(String idOrden) throws Exception {
        Orden orden = obtenerOrden(idOrden);
        ordenRepo.delete(orden);
        return "La orden ha sido eliminada.";
    }

    @Override
    public List<Orden> buscarOrdenesPorCliente(String idCliente) throws Exception {
        return ordenRepo.buscarOrdenesPorCliente(idCliente);
    }

    @Override
    public List<Orden> buscarOrdenesPorRangoDeFechas(String fechaInicio, String fechaFin) throws Exception {

        SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
        Date dateOne=new Date();
        Date dateTwo=new Date();
        try {
            dateOne=parser.parse(fechaInicio);
            dateTwo=parser.parse(fechaFin);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        if(dateOne.before(dateTwo)){
            return ordenRepo.buscarOrdenesPorRangoDeFechas(dateOne, dateTwo);
        }else{
            List<Orden> ordenList = new ArrayList<>();
            return ordenList;
        }

    }

    @Override
    public InformacionOrdenDTO obtenerInformacionOrden(String idOrden) throws Exception {
        Orden orden = obtenerOrden(idOrden);
        return new InformacionOrdenDTO(
                orden.getId(),
                orden.getIdCliente(),
                orden.getFecha(),
                orden.getTotal(),
                orden.getItems()


        );
    }

    @Override
    public List<InformacionOrdenDTO> listarTodasLasOrdenes() throws Exception {
        List<Orden> ordenes = ordenRepo.findAll();
        return ordenes.stream()
                .map(orden -> new InformacionOrdenDTO(
                        orden.getId(),
                        orden.getIdCliente(),
                        orden.getFecha(),
                        orden.getTotal(),
                        orden.getItems()))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<InformacionOrdenDTO> listarOrdenesPorCliente(String idCliente) throws Exception {
//        List<Orden> ordenes = ordenRepo.buscarOrdenesPorCliente(idCliente);
//        return ordenes.stream()
//                .map(orden -> new InformacionOrdenDTO(
//                        orden.getId(),
//                        orden.getIdCliente(),
//                        orden.getFecha(),
//                        orden.getTotal(),
//                        orden.getItems()))
//                .collect(Collectors.toList());
//    }





    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setId(payment.getId().toString());
        pago.setFecha( payment.getDateCreated().toLocalDateTime() );
        pago.setEstado(payment.getStatus());
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(payment.getPaymentTypeId());
        pago.setMoneda(payment.getCurrencyId());
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(payment.getTransactionAmount().floatValue());
        return pago;
    }


    private Orden obtenerOrden(String idOrden) throws Exception {
        Optional<Orden> ordenOptional = ordenRepo.findById(idOrden);
        if (ordenOptional.isEmpty()) {
            throw new Exception("No se encontró una orden con el ID " + idOrden);
        }
        return ordenOptional.get();
    }

    @Override
    public Preference realizarPago(String idOrden) throws Exception {


        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Orden ordenGuardada = obtenerOrden(idOrden);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(DetalleOrden item : ordenGuardada.getItems()){


            // Obtener el evento y la localidad del ítem
            Evento evento = eventoServicio.obtenerEvento(item.getIdEvento().toString());
            Localidad localidad = evento.obtenerLocalidad(item.getNombreLocalidad());


            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombre())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipo().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(localidad.getPrecio()))
                            .build();


            itemsPasarela.add(itemRequest);
        }


        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("TEST-5343511780372752-100811-c23f39c427f70f650a67ceaaf25513a2-2018651974");


        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();


        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_orden", ordenGuardada.getId()))
                .notificationUrl("https://2332-45-229-73-135.ngrok-free.app/api/orden/notificacion-pago")
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        ordenGuardada.setCodigoPasarela( preference.getId() );
        ordenRepo.save(ordenGuardada);


        return preference;
    }



    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {


            // Obtener el tipo de notificación
            Object tipo = request.get("type");


            // Si la notificación es de un pago entonces obtener el pago y la orden asociada
            if ("payment".equals(tipo)) {


                // Capturamos el JSON que viene en el request y lo convertimos a un String
                String input = request.get("data").toString();


                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");


                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient client = new PaymentClient();
                Payment payment = client.get( Long.parseLong(idPago) );


                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idOrden = payment.getMetadata().get("id_orden").toString();


                // Se obtiene la orden guardada en la base de datos y se le asigna el pago
                Orden orden = obtenerOrden(idOrden);
                Pago pago = crearPago(payment);
                orden.setPago(pago);
                ordenRepo.save(orden);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
