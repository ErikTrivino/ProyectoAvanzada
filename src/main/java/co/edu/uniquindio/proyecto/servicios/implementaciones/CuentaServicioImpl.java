package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.config.JWTUtils;
import co.edu.uniquindio.proyecto.modelo.documentos.*;
import co.edu.uniquindio.proyecto.modelo.dto.evento.ItemEventoDTO;
import co.edu.uniquindio.proyecto.modelo.enums.*;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;
import co.edu.uniquindio.proyecto.modelo.dto.email.EmailDTO;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import co.edu.uniquindio.proyecto.repositorios.CarritoRepo;
import co.edu.uniquindio.proyecto.repositorios.CuentaRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CarritoServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EmailServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EventoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CuentaServicioImpl implements CuentaServicio {

    private final CuentaRepo cuentaRepo;
    private final JWTUtils jwtUtils;
    private final EmailServicio emailServicio;

    private final EventoServicio eventoServicio;
    private final CarritoRepo carritoRepo;
    //private final FutureOrPresentValidatorForLocalDateTime futureOrPresentValidatorForLocalDateTime;


    @Override
    public String crearCuenta(CrearCuentaDTO cuenta) throws Exception {


       if( existeEmail(cuenta.correo())){

            throw new Exception("Ya existe un usuario registrado con el correo "+cuenta.correo());

       }

       if( existeCedula(cuenta.cedula())){

           throw new Exception("La cédula " + cuenta.cedula() + " ya se encuentra registrada.");

       }

        String codigoAleatorio = generarCodigo();

        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setEmail(cuenta.correo());
        nuevaCuenta.setPassword(encriptarPassword(cuenta.password()));
        nuevaCuenta.setRol(Rol.CLIENTE);
        nuevaCuenta.setFechaRegistro(LocalDateTime.now());
        nuevaCuenta.setUsuario(new Usuario(
                cuenta.cedula(),
                cuenta.nombre(),
                cuenta.telefono(),
                cuenta.direccion()

        ));
        nuevaCuenta.setEstado(EstadoCuenta.INACTIVO);
        nuevaCuenta.setCodigoValidacionRegistro(
                new CodigoValidacion(
                        LocalDateTime.now(), codigoAleatorio
                ));
        nuevaCuenta.setBoletas(new ArrayList<>());
        nuevaCuenta.setPreferencias(new ArrayList<>());

        Carrito carrito = new Carrito();
        carrito.setFecha(LocalDateTime.now());
        carrito.setItems(new ArrayList<>());
        carrito.setId(nuevaCuenta.getId());
        carrito.setPrecioTotal(0);
        carritoRepo.save(carrito);
        cuentaRepo.save(nuevaCuenta);
        emailServicio.enviarCorreo( new EmailDTO("CODIGO DE ACTIVACIÓN CUENTA", nuevaCuenta.getCodigoValidacionRegistro().getCodigo(), nuevaCuenta.getEmail()) );
        return "Su cuenta se ha generado con éxito.";
    }

    @Override
    public String editarCuenta(EditarCuentaDTO cuenta) throws Exception {

        //Si no se encontró la cuenta del usuario, lanzamos una excepción
        if(!existeCuenta(cuenta.id())){
            throw new Exception("No se encontró una cuenta con el id "+cuenta.id());
        }


        Cuenta cuentaModificada = obtenerCuenta(cuenta.id());
        cuentaModificada.getUsuario().setNombre( cuenta.nombre());
        cuentaModificada.getUsuario().setDireccion( cuenta.direccion());
        cuentaModificada.getUsuario().setTelefono( cuenta.telefono());

        cuentaRepo.save(cuentaModificada);
        return cuentaModificada.getId();
    }


    @Override
    public String eliminarCuenta(String id) throws Exception {

        if(!existeCuenta(id)){
            throw new Exception("No se encontró una cuenta con el id " + id);
        }

        Cuenta cuenta = obtenerCuenta(id);

        cuenta.setEstado(EstadoCuenta.ELIMINADO);

        cuentaRepo.save(cuenta);

        return "Su cuenta ha sido eliminada.";
    }

    @Override
    @Transactional (readOnly = true)
    public InformacionCuentaDTO obtenerInformacionCuenta(String id) throws Exception {

        Cuenta cuenta = obtenerCuenta(id);

        return new InformacionCuentaDTO(
                id,
                cuenta.getUsuario().getCedula(),
                cuenta.getUsuario().getNombre(),
                cuenta.getUsuario().getTelefono(),
                cuenta.getUsuario().getDireccion(),
                cuenta.getEmail(),
                cuenta.getBoletas()
        );

    }

    @Override
    public String enviarCodigoRecuperacionPassword(String correo) throws Exception {

        Cuenta cuenta = obtenerEmail(correo);
        String codigoValidacion = generarCodigo();

        cuenta.setCodigoValidacionPassword(new CodigoValidacion(
                LocalDateTime.now(),
                codigoValidacion
                ));

        cuentaRepo.save(cuenta);

        emailServicio.enviarCorreo( new EmailDTO("CODIGO DE RECUPERACION DE CONTRASEÑA", codigoValidacion, correo) );

        return "Se ha enviado un correo con el código de recuperación de contraseña";

    }

    @Override
    public String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {

        Cuenta cuentaOptional = obtenerEmail(cambiarPasswordDTO.correo());

        CodigoValidacion codigoValidacion = cuentaOptional.getCodigoValidacionPassword();

        if(codigoValidacion.getCodigo().equals(cambiarPasswordDTO.codigoVerificacion())){
            if(codigoValidacion.getFechaCreacion().plusMinutes(15).isAfter(LocalDateTime.now())){
                cuentaOptional.setPassword(encriptarPassword(cambiarPasswordDTO.passwordNueva()));
                cuentaRepo.save(cuentaOptional);
            }else{
                throw new Exception("El código ya expiró.");
            }
        }else{
            throw new Exception("El código ingresado no coincide con el enviado al correo.");
        }

        return "Su contraseña ha sido cambiada.";
    }

    @Override
    public TokenDTO iniciarSesion(LoginDTO loginDTO) throws Exception {

        Cuenta cuenta = obtenerPorEmail(loginDTO.correo());
        if(cuenta.getEstado() == EstadoCuenta.ACTIVO){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if( !passwordEncoder.matches(loginDTO.password(), cuenta.getPassword()) ) {
                throw new Exception("La contraseña es incorrecta");
            }

            Map<String, Object> map = construirClaims(cuenta);
            return new TokenDTO( jwtUtils.generarToken(cuenta.getEmail(), map) );
        }else {
            throw new Exception("La cuenta no esta activa");
        }

    }


    @Override
    public String activarCuenta(ActivarCuentaDTO activarCuentaDTO) throws Exception {
        // Buscar la cuenta por el token de validación de registro
        Optional<Cuenta> cuentaOpt = cuentaRepo.buscarPorCodigoValidacion(activarCuentaDTO.token());

        // Verificar si la cuenta existe
        if (!cuentaOpt.isPresent()) {
            throw new Exception("El token de activación es inválido.");
        }

        if (cuentaOpt.get().getEstado() == EstadoCuenta.ACTIVO){
            throw new Exception("La cuenta ya está activa.");
        }

        Cuenta cuenta = cuentaOpt.get();
        // Verificar si el tiempo desde la creación del token ha superado los 15 minutos
        LocalDateTime fechaCreacionToken = cuenta.getCodigoValidacionRegistro().getFechaCreacion();
        if (fechaCreacionToken.plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new Exception("El token de activación ha expirado.");
        }

        // Activar la cuenta si el token es válido y no ha expirado
        cuenta.setEstado(EstadoCuenta.ACTIVO);
        if(!cuentaOpt.get().isActivacionPrimeraVez()){
            String nombreCupon = "CUPON"+generarNumeroAleatorio();
            Cupon cupon = new Cupon();
            cupon.setCodigo(nombreCupon);
            cupon.setTipo(TipoCupon.UNICO);
            cupon.setDescuento(15.0f);
            cupon.setNombre("Cupon de activacion primera vez");
            cupon.setFechaVencimiento(LocalDate.now().minusMonths(1));
            cupon.setEstado(EstadoCupon.ACTIVO);
            String cuerpo = "<!DOCTYPE html>\n" +
                    "<html lang=\"es\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Activación de Cuenta - Cupón de Descuento</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "            color: #333;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            width: 100%;\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 0 auto;\n" +
                    "            background-color: #fff;\n" +
                    "            padding: 20px;\n" +
                    "            border-radius: 8px;\n" +
                    "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                    "        }\n" +
                    "        .header {\n" +
                    "            background-color: #4CAF50;\n" +
                    "            color: #fff;\n" +
                    "            padding: 10px;\n" +
                    "            text-align: center;\n" +
                    "            border-radius: 8px 8px 0 0;\n" +
                    "        }\n" +
                    "        .content {\n" +
                    "            padding: 20px;\n" +
                    "            line-height: 1.6;\n" +
                    "        }\n" +
                    "        .coupon {\n" +
                    "            font-size: 20px;\n" +
                    "            font-weight: bold;\n" +
                    "            color: #4CAF50;\n" +
                    "            margin: 20px 0;\n" +
                    "        }\n" +
                    "        .footer {\n" +
                    "            font-size: 12px;\n" +
                    "            color: #777;\n" +
                    "            text-align: center;\n" +
                    "            padding: 10px;\n" +
                    "            border-top: 1px solid #ddd;\n" +
                    "            margin-top: 20px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <div class=\"header\">\n" +
                    "            <h1>¡Bienvenido a nuestra comunidad!</h1>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            <p>Estimado usuario,</p>\n" +
                    "            <p>Gracias por activar tu cuenta con nosotros. Como agradecimiento, te regalamos un cupón de descuento exclusivo para tu primera compra.</p>\n" +
                    "\n" +
                    "            <p><strong>Detalles del Cupón:</strong></p>\n" +
                    "            <p class=\"coupon\">Código del Cupón: <span style=\"color:#333;\">{{"+nombreCupon+"}}</span></p>\n" +
                    "            <ul>\n" +
                    "                <li><strong>Tipo de Cupón:</strong> Único</li>\n" +
                    "                <li><strong>Descuento:</strong> 15%</li>\n" +
                    "                <li><strong>Nombre:</strong> Cupón de activación primera vez</li>\n" +
                    "                <li><strong>Fecha de Vencimiento:</strong> {{"+cupon.getFechaVencimiento().toString()+"}}</li>\n" +
                    "                <li><strong>Estado:</strong> Activo</li>\n" +
                    "            </ul>\n" +
                    "\n" +
                    "            <p>¡Aprovecha este descuento y explora todos los beneficios que tenemos para ti!</p>\n" +
                    "        </div>\n" +
                    "        <div class=\"footer\">\n" +
                    "            <p>Si tienes alguna pregunta, no dudes en contactarnos.</p>\n" +
                    "            <p>&copy; 2024 - Nuestra Empresa. Todos los derechos reservados.</p>\n" +
                    "        </div>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>\n";

            emailServicio.enviarCorreoHtml( new EmailDTO("CUPON POR ACTIVACION CUENTA PRIMERA VEZ", cuerpo, cuentaOpt.get().getEmail()) );
        }

        cuentaRepo.save(cuenta); // Guardar el cambio en la base de datos

        return "Cuenta activada exitosamente.";
    }

    @Override
    public List<Boleta> buscarBoletaPorNombreEvento(String nombreEvento) throws Exception {
        List<Cuenta> cuentas = cuentaRepo.buscarBoletaPorNombreEvento(nombreEvento);

        return cuentas.stream()
                .flatMap(cuenta -> cuenta.getBoletas().stream())
                .filter(boleta -> nombreEvento.equals(boleta.getNombreEvento()))
                .collect(Collectors.toList());
    }

    public static int generarNumeroAleatorio() {
        Random random = new Random();
        return random.nextInt(10000); // Genera un número entre 0 y 9999
    }

    @Override
    public List<ItemCuentaDTO> listarCuentas() {


        //Obtenemos todas las cuentas de los usuarios de la base de datos
        List<Cuenta> cuentas = cuentaRepo.findAll();

        //Creamos una lista de DTOs
        List<ItemCuentaDTO> items = new ArrayList<>();


        //Recorremos la lista de cuentas y por cada uno creamos un DTO y lo agregamos a la lista
        for (Cuenta cuenta : cuentas) {
            items.add( new ItemCuentaDTO(
                    cuenta.getId(),
                    cuenta.getUsuario().getNombre(),
                    cuenta.getEmail(),
                    cuenta.getUsuario().getTelefono()
            ));
        }


        return items;
    }

    @Override
    public Cuenta obtenerPorEmail(String email) throws Exception {

       // System.out.println(correo);

        Optional<Cuenta> cuentaOptional = cuentaRepo.findByEmail(email);

       // System.out.println(cuentaOptional.isEmpty());

        if(cuentaOptional.isEmpty()){
            throw new Exception("No existe una cuenta registrada con el correo " + email + ".");
        }

        Cuenta cuenta = cuentaOptional.get();

        if(cuenta.getEstado() == EstadoCuenta.ELIMINADO){
            throw new Exception("La cuenta registrada con el correo " + email + " esta ELIMINADA.");
        }

        return cuenta;

    }

    @Override
    public String enviarCodigoActivacionCuenta(String correo) throws Exception {

        Cuenta cuenta = obtenerEmail(correo);
        String codigoValidacion = generarCodigo();

        cuenta.setCodigoValidacionRegistro(new CodigoValidacion(
                LocalDateTime.now(),
                codigoValidacion
        ));

        cuentaRepo.save(cuenta);

        emailServicio.enviarCorreo( new EmailDTO("CODIGO DE ACTIVACIÓN CUENTA", codigoValidacion, correo) );

        return "Se ha enviado un correo con el código de activación de su cuenta";

    }

    @Override
    public void eliminarBoletas(String idCuenta, List<Boleta> boletasAEliminar) throws Exception {
        // Buscar la cuenta por su ID
        Optional<Cuenta> optionalCuenta = cuentaRepo.findById(idCuenta);

        // Validar si la cuenta existe
        if (optionalCuenta.isEmpty()) {
            throw new Exception("La cuenta con ID " + idCuenta + " no existe.");
        }

        Cuenta cuenta = optionalCuenta.get();

        // Obtener la lista de boletas del cliente
        List<Boleta> boletasCliente = cuenta.getBoletas();

        // Filtrar y eliminar las boletas que coinciden con la lista proporcionada
        boletasCliente.removeIf(boletaCliente ->
                boletasAEliminar.stream().anyMatch(boletaAEliminar ->
                        boletaCliente.getIdBoleta().equals(boletaAEliminar.getIdBoleta())
                )
        );

        // Guardar la cuenta actualizada en el repositorio
        cuentaRepo.save(cuenta);
    }


    private Cuenta obtenerEmail(String correo) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.buscaremail(correo);

        if(cuentaOptional.isEmpty()){
            throw new Exception("El correo dado no está registrado.");
        }

        Cuenta cuenta = cuentaOptional.get();

        if(cuenta.getEstado().equals(EstadoCuenta.ELIMINADO)){
            throw new Exception("La cuenta registrada con el correo " + correo + " esta ELIMINADA.");
        }

        return cuenta;
    }

    private Cuenta obtenerCuenta(String id) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.findById(id);

        if(cuentaOptional.isEmpty()){
            throw new Exception("No existe una cuenta registrada con el id " + id + ".");
        }

        Cuenta cuenta = cuentaOptional.get();

        if(cuenta.getEstado().equals(EstadoCuenta.ELIMINADO)){
            throw new Exception("La cuenta registrada con el correo " + id + " esta ELIMINADA.");
        }

        return cuenta;
    }


    private boolean existeCuenta(String cuenta) {

        Optional<Cuenta> optionalCuenta = cuentaRepo.findById(cuenta);

        if (optionalCuenta.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    private boolean existeCedula(String cedula) {
        return cuentaRepo.buscarCedula(cedula).isPresent();
    }

    private boolean existeCorreo(String correo) {

        return cuentaRepo.buscaremail(correo).isPresent();

    }

    private boolean existeEmail(String email) {
        return cuentaRepo.buscaremail(email).isPresent();
    }

    private String generarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder codigo = new StringBuilder();

        for(int i = 0; i < 6; i++){
            int indice = (int) (caracteres.length() * Math.random());
            codigo.append(caracteres.charAt(indice));
        }

        return codigo.toString();
    }

    private String encriptarPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode( password );
    }

    private Map<String, Object> construirClaims(Cuenta cuenta) {
        return Map.of(
                "rol", cuenta.getRol(),
                "nombre", cuenta.getUsuario().getNombre(),
                "id", cuenta.getId()
        );
    }

    ///NUEVA FUNCIONALIDAD
    @Override
    public List<Boleta> buscarBoletasPorPropietario(String idPropietario) throws Exception {
        Cuenta cuenta = obtenerCuentaPorIdPropietario(idPropietario);

        return cuenta.getBoletas();
    }


    @Override
    public Boleta obtenerDetalleBoleta(String idBoleta, String idPropietario) throws Exception {
        Optional<Cuenta> cuenta = cuentaRepo.findById(idPropietario);
        if(cuenta.isPresent()){
            return cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getIdBoleta().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<Boleta> listarBoletasEnviadas(String idPropietario) throws Exception {
        Cuenta cuenta = obtenerCuentaPorIdPropietario(idPropietario);
        return cuenta.getBoletas().stream()
                .filter(boleta -> boleta.getIdPropietarioOriginal().equals(idPropietario) && boleta.getEstado()== EstadoBoleta.ENVIADA)
                .collect(Collectors.toList());
    }

    @Override
    public List<Boleta> listarBoletasPendientes(String idPropietario) throws Exception {
        Cuenta cuenta = obtenerCuentaPorIdPropietario(idPropietario);
        //System.out.println(cuenta.getBoletas().toString());
        return cuenta.getBoletas().stream()
                .filter(boleta -> boleta.getIdPropietarioOriginal().equals(idPropietario) && boleta.getEstado() == EstadoBoleta.PENDIENTE)
                .collect(Collectors.toList());
    }

    @Override
    public void transferirBoleta(String idBoleta, String idPropietario, String idNuevoPropietario) throws Exception {

        Optional<Cuenta> cuenta = cuentaRepo.findById(idPropietario);
        //DESDE EL FRONT LLEGA EL CORREO
        Optional<Cuenta> cuenta2 = cuentaRepo.findByEmail(idNuevoPropietario);


        if(cuenta.isPresent() && cuenta2.isPresent()){
            Boleta boletaTra = cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getIdBoleta().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
            if(boletaTra != null){
                if(boletaTra.getEstado() == EstadoBoleta.ENVIADA){
                    throw new Exception("La boleta a tranferir ya ha sido enviada");
                }else {
                    if(boletaTra.getEstado() == EstadoBoleta.PENDIENTE){
                        throw new Exception("La boleta a tranferir ya tiene como etado pendiente");
                    }else{
                        boletaTra.setEstado(EstadoBoleta.ENVIADA);
                        cuentaRepo.save(cuenta.get());

                        boletaTra.setEstado(EstadoBoleta.PENDIENTE);

                        cuenta2.get().getBoletas().add(boletaTra);
                        cuentaRepo.save(cuenta2.get());
                    }

                }

            }
        }

    }

    @Override
    public void aceptarBoleta(String idBoleta, String idNuevoPropietario) throws Exception {

        Optional<Cuenta> cuenta = cuentaRepo.findById(idNuevoPropietario);
        if(cuenta.isPresent()){
            Boleta boletaTra = cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getIdBoleta().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
            if(boletaTra != null){
                cuenta.get().getBoletas().remove(boletaTra);
                boletaTra.setEstado(EstadoBoleta.ACEPTADA);
                cuenta.get().getBoletas().add(boletaTra);
                cuentaRepo.save(cuenta.get());

            }
        }

    }

    @Override
    public void agregarBoletas(String idCuenta, List<Boleta> boletas) throws Exception {
        Optional<Cuenta> cuenta = cuentaRepo.findById(idCuenta);
        cuenta.get().setBoletas(boletas);
        cuentaRepo.save(cuenta.get());
    }


    private Boleta buscarBoletaPorId(String idBoleta) throws Exception {
        // Lógica para buscar la boleta en las cuentas
        Optional<Cuenta> cuentaOptional = cuentaRepo.findAll().stream()
                .filter(cuenta -> cuenta.getBoletas().stream().anyMatch(boleta -> boleta.getIdBoleta().equals(idBoleta)))
                .findFirst();

        if (cuentaOptional.isPresent()) {
            return cuentaOptional.get().getBoletas().stream()
                    .filter(boleta -> boleta.getIdBoleta().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    private Cuenta obtenerCuentaPorIdPropietario(String idPropietario) throws Exception {
        Optional<Cuenta> cuentaOptional = cuentaRepo.findById(idPropietario);
        if (cuentaOptional.isEmpty()) {
            throw new Exception("No existe una cuenta con el propietario " + idPropietario);
        }
        return cuentaOptional.get();
    }

    //FUNCIONALIDAD DOS
    @Override
    public List<TipoEvento> obtenerPreferencias() throws Exception {
        return List.of(TipoEvento.values());
    }

    @Override
    public void agregarPreferenciasUsuario(String idUsuario, List<TipoEvento> tipoPreferencias) throws Exception {
        Optional<Cuenta> cuenta = cuentaRepo.findById(idUsuario);

        if(!cuenta.isPresent()){
            throw new Exception("No se encontro la cuenta");
        }


        cuenta.get().setPreferencias(new ArrayList<>());
        cuenta.get().getPreferencias().addAll(tipoPreferencias);
        cuentaRepo.save(cuenta.get());
    }
    @Override
    public List<ItemEventoDTO> obtenerPreferenciasUsuario(String idUsuario) throws Exception {
        // Verificar si el usuario existe en la base de datos
        Cuenta usuario = cuentaRepo.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        List<Evento> eventos = eventoServicio.traerEventosPorPreferenciaUsuario(usuario.getPreferencias());

        List<ItemEventoDTO> items = new ArrayList<>();
        for (Evento evento: eventos
             ) {
            ItemEventoDTO itemEventoDTO = new ItemEventoDTO(evento.getId(), evento.getImagenPortada(), evento.getNombre(), evento.getFechaEvento(), evento.getCiudad());
            items.add(itemEventoDTO);
        }
        // Devolver las preferencias del usuario
        return items;
    }





}
