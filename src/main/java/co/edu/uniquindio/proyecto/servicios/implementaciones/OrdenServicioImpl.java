package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.InformacionCuentaDTO;
import co.edu.uniquindio.proyecto.modelo.dto.email.EmailDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.CrearOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.EditarOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.orden.InformacionOrdenDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoBoleta;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.vo.DetalleOrden;
import co.edu.uniquindio.proyecto.modelo.vo.Localidad;
import co.edu.uniquindio.proyecto.modelo.vo.Pago;
import co.edu.uniquindio.proyecto.repositorios.OrdenRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EmailServicio;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenServicioImpl  implements OrdenServicio {

    private final OrdenRepo ordenRepo;
    private final EventoServicio eventoServicio;
    private final CuentaServicio cuentaServicio;
    private final EmailServicio emailServicio;

    @Override
    public String crearOrden(CrearOrdenDTO crearOrdenDTO) throws Exception {
        LocalDate fechaActual = LocalDate.now();

        // Recorrer cada item en crearOrdenDTO para realizar las validaciones por evento y localidad
        for (DetalleOrden detalle : crearOrdenDTO.items()) {
            // Obtener el evento correspondiente para cada item
            Evento evento = eventoServicio.obtenerEvento(detalle.getIdEvento());

            // Validar que la compra solo se pueda realizar hasta dos días antes del evento
            if (evento.getFechaEvento().minusDays(2).isBefore(fechaActual)) {
                throw new Exception("La compra solo puede realizarse hasta dos días antes del evento: " + evento.getNombre());
            }

            // Obtener la localidad específica dentro del evento
            Localidad localidad = evento.obtenerLocalidad(detalle.getNombreLocalidad());

            // Validar que haya suficiente capacidad en la localidad solicitada
            if (localidad.getCapacidadDisponible() < detalle.getCantidad()) {
                throw new Exception("No hay capacidad suficiente para la localidad " + detalle.getNombreLocalidad() + " en el evento " + evento.getNombre());
            }
        }

        // Crear y guardar la orden si todas las validaciones pasan
        Orden nuevaOrden = new Orden();
        nuevaOrden.setIdCliente(crearOrdenDTO.idCliente());
        nuevaOrden.setFecha(fechaActual);
        nuevaOrden.setCodigoPasarela(crearOrdenDTO.codigoPasarela());
        nuevaOrden.setItems(crearOrdenDTO.items());
        nuevaOrden.setTotal(crearOrdenDTO.total());

        // Descontar la capacidad en las localidades y actualizar el evento
        for (DetalleOrden detalle : crearOrdenDTO.items()) {
            Evento evento = eventoServicio.obtenerEvento(detalle.getIdEvento());
            Localidad localidad = evento.obtenerLocalidad(detalle.getNombreLocalidad());

            // Actualizar la cantidad de entradas vendidas en la localidad
            localidad.setEntradasVendidas(localidad.getEntradasVendidas() + detalle.getCantidad());
        }

        // Guardar la orden en la base de datos
        ordenRepo.save(nuevaOrden);
        // Generar boletas a partir de los detalles de la orden
        List<Boleta> boletasGeneradas = new ArrayList<>();
        for (DetalleOrden detalle : crearOrdenDTO.items()) {
            Evento evento = eventoServicio.obtenerEvento(detalle.getIdEvento());
            Localidad localidad = evento.obtenerLocalidad(detalle.getNombreLocalidad());

            // Actualizar la cantidad de entradas vendidas en la localidad
            localidad.setEntradasVendidas(localidad.getEntradasVendidas() + detalle.getCantidad());

            // Crear boletas para cada entrada en el detalle
            for (int i = 0; i < detalle.getCantidad(); i++) {
                Boleta boleta = new Boleta();
                boleta.setIdBoleta(UUID.randomUUID().toString()); // Generar un ID único para la boleta
                boleta.setIdEvento(detalle.getIdEvento());
                boleta.setIdClientePropietario(crearOrdenDTO.idCliente());
                boleta.setNombreEvento(evento.getNombre());
                boleta.setFechaEvento(evento.getFechaEvento());
                boleta.setNombreLocalidad(detalle.getNombreLocalidad());
                boleta.setEstado(EstadoBoleta.ACTIVA);
                boleta.setIdPropietarioOriginal(crearOrdenDTO.idCliente());

                // Guardar la boleta en la base de datos
                //boletaRepo.save(boleta);
                boletasGeneradas.add(boleta);
            }
        }

        cuentaServicio.agregarBoletas(crearOrdenDTO.idCliente(), boletasGeneradas);


        InformacionCuentaDTO cuenta = cuentaServicio.obtenerInformacionCuenta(crearOrdenDTO.idCliente());
        // Enviar correo de confirmación
        String correoPrueba = "unieventosfae@gmail.com";
        EmailDTO emailDTO = new EmailDTO(
                cuenta.correo(),
                "Detalles de tu compra en UniEventos", // Asunto del correo
                "Gracias por tu compra. Adjuntamos el código QR de tu orden y los detalles de la misma." // Cuerpo del correo
        );

        // Enviar el correo con el código QR adjunto
        emailServicio.enviarCorreoConQr(emailDTO, nuevaOrden);

        return "La orden ha sido creada con éxito y se ha enviado un correo con los detalles de la compra.";
    }




    @Override
    public String actualizarOrden(EditarOrdenDTO editarOrdenDTO) throws Exception {
        Orden orden = obtenerOrden(editarOrdenDTO.id());

        orden.setItems(editarOrdenDTO.items());

        orden.setTotal(editarOrdenDTO.total());
        orden.setIdCliente(editarOrdenDTO.idCliente());

        orden.setIdCupon(editarOrdenDTO.idCupon());

        ordenRepo.save(orden);
        return "La orden ha sido actualizada con éxito.";
    }

   /* @Override
    public String eliminarOrden(String idOrden) throws Exception {
        Orden orden = obtenerOrden(idOrden);

        // Obtener el evento asociado para devolver la capacidad
        Evento evento = eventoServicio.obtenerEvento(orden.getItems().get(0).getIdEvento());

        // Devolver la capacidad a las localidades
        for (DetalleOrden detalle : orden.getItems()) {
            Localidad localidad = evento.obtenerLocalidad(detalle.getNombreLocalidad());
            localidad.setEntradasVendidas(localidad.getEntradasVendidas() - detalle.getCantidad());
        }

        ordenRepo.delete(orden);
        return "La orden ha sido cancelada y la capacidad ha sido devuelta.";
    }*/
   @Override
   public String eliminarOrden(String idOrden) throws Exception {
       // Obtener la orden a partir del ID proporcionado
       Orden orden = obtenerOrden(idOrden);
       if (orden == null) {
           throw new Exception("La orden no existe.");
       }

       // Obtener la cuenta del cliente que realizó la orden
       InformacionCuentaDTO cuenta = cuentaServicio.obtenerInformacionCuenta(orden.getIdCliente());
       if (cuenta == null) {
           throw new Exception("No se encontró la cuenta del cliente.");
       }

       // Recuperar las boletas asociadas a la cuenta del cliente
       List<Boleta> boletasCliente = cuentaServicio.buscarBoletasPorPropietario(cuenta.id());

       // Crear una lista para almacenar las boletas a eliminar
       List<Boleta> boletasAEliminar = new ArrayList<>();

       // Devolver la capacidad a las localidades y buscar las boletas que corresponden a esta orden
       for (DetalleOrden detalle : orden.getItems()) {
           Evento evento = eventoServicio.obtenerEvento(detalle.getIdEvento());
           if (evento == null) {
               throw new Exception("Evento no encontrado para el detalle: " + detalle.getIdEvento());
           }

           Localidad localidad = evento.obtenerLocalidad(detalle.getNombreLocalidad());
           if (localidad == null) {
               throw new Exception("Localidad no encontrada: " + detalle.getNombreLocalidad());
           }

           // Devolver la capacidad a la localidad
           localidad.setEntradasVendidas(localidad.getEntradasVendidas() - detalle.getCantidad());

           // Buscar y eliminar las boletas que coinciden con el detalle de la orden
           int boletasEncontradas = 0;
           for (Boleta boleta : boletasCliente) {
               if (boleta.getIdEvento().equals(detalle.getIdEvento()) &&
                       boleta.getNombreLocalidad().equals(detalle.getNombreLocalidad()) &&
                       boleta.getEstado() == EstadoBoleta.ACTIVA) {

                   // Agregar boleta a la lista para eliminar
                   boletasAEliminar.add(boleta);
                   boletasEncontradas++;

                   // Si encontramos todas las boletas que corresponden a la cantidad comprada, salimos del bucle
                   if (boletasEncontradas == detalle.getCantidad()) {
                       break;
                   }
               }
           }

           // Validar que hayamos encontrado suficientes boletas para eliminar
           if (boletasEncontradas < detalle.getCantidad()) {
               throw new Exception("No se encontraron suficientes boletas para el detalle del evento " + detalle.getIdEvento());
           }
       }

       // Eliminar las boletas de la cuenta del cliente
       cuentaServicio.eliminarBoletas(cuenta.id(), boletasAEliminar);

       // Finalmente, eliminar la orden de la base de datos
       ordenRepo.delete(orden);

       return "La orden ha sido cancelada, las boletas han sido eliminadas y la capacidad ha sido devuelta.";
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

    @Override
    public Preference realizarPago(String idOrden) throws Exception {


        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Orden ordenGuardada = obtenerOrden(idOrden);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();


        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(DetalleOrden item : ordenGuardada.getItems()){

            //System.out.println();

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
        MercadoPagoConfig.setAccessToken("TEST-588868403287290-100616-0e528ecb48f60cebab6692df2b492cec-1615533331");


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
                .notificationUrl("https://83dc-2800-e2-6f80-309-bda6-292a-7535-e863.ngrok-free.app/api/general/notificacion-pago")//URL TOMADA DEL NGROK
                .build();


        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);


        // Guardar el código de la pasarela en la orden
        ordenGuardada.setCodigoPasarela( preference.getId() );
      //  System.out.println(ordenGuardada.getPago().toString());
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

    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setIdPago(payment.getId().toString());
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
    public List<InformacionOrdenDTO> obtenerHistorialOrdenes(String idCliente) throws Exception {
        // Verificar si el cliente tiene órdenes
        List<Orden> ordenesCliente = ordenRepo.buscarOrdenesPorCliente(idCliente);

        if (ordenesCliente.isEmpty()) {
            throw new Exception("No se encontraron órdenes para el cliente con ID " + idCliente);
        }

        // Mapear las órdenes a objetos DTO para el historial
        return ordenesCliente.stream()
                .map(orden -> new InformacionOrdenDTO(
                        orden.getId(),
                        orden.getIdCliente(),
                        orden.getFecha(),
                        orden.getTotal(),
                        orden.getItems()
                ))
                .collect(Collectors.toList());
    }

}
