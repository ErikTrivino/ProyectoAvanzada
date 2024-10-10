package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.config.JWTUtils;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoBoleta;
import co.edu.uniquindio.proyecto.modelo.dto.email.EmailDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCuenta;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import co.edu.uniquindio.proyecto.repositorios.CuentaRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.proyecto.servicios.interfaces.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CuentaServicioImpl implements CuentaServicio {

    private final CuentaRepo cuentaRepo;
    private final JWTUtils jwtUtils;
    private final EmailServicio emailServicio;
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

        cuentaRepo.save(nuevaCuenta);
        //SE DEBE ENVIAR AL CORREO DEL USUARIO EL CODIGO DE VALIDACIÓN
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

        Cuenta cuentaOptional = obtenerEmail(cambiarPasswordDTO.email());

        CodigoValidacion codigoValidacion = cuentaOptional.getCodigoValidacionPassword();

        if(codigoValidacion.getCodigo().equals(cambiarPasswordDTO.codigoVerificacion())){
            if(codigoValidacion.getFechaCreacion().plusMinutes(15).isAfter(LocalDateTime.now())){
                cuentaOptional.setPassword(encriptarPassword(cambiarPasswordDTO.passwordNueva()));
                cuentaRepo.save(cuentaOptional);
            }else{
                throw new Exception("El código ya expiró.");
            }
        }else{
            throw new Exception("El código ingresado no coincide con el enviado al email.");
        }

        return "Su contraseña ha sido cambiada.";
    }

    @Override
    public TokenDTO iniciarSesion(LoginDTO loginDTO) throws Exception {

        Cuenta cuenta = obtenerPorEmail(loginDTO.correo());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if( !passwordEncoder.matches(loginDTO.password(), cuenta.getPassword()) ) {
            throw new Exception("La contraseña es incorrecta");
        }

        Map<String, Object> map = construirClaims(cuenta);
        return new TokenDTO( jwtUtils.generarToken(cuenta.getEmail(), map) );
    }


    @Override
    public String activarCuenta(ActivarCuentaDTO activarCuentaDTO) throws Exception {
        // Buscar la cuenta por el token de validación de registro
        Optional<Cuenta> cuentaOpt = cuentaRepo.buscarPorCodigoValidacion(activarCuentaDTO.token());

        // Verificar si la cuenta existe
        if (!cuentaOpt.isPresent()) {
            throw new Exception("El token de activación es inválido.");
        }

        Cuenta cuenta = cuentaOpt.get();
        // Verificar si el tiempo desde la creación del token ha superado los 15 minutos
        LocalDateTime fechaCreacionToken = cuenta.getCodigoValidacionRegistro().getFechaCreacion();
        if (fechaCreacionToken.plusMinutes(15).isBefore(LocalDateTime.now())) {
            throw new Exception("El token de activación ha expirado.");
        }

        // Activar la cuenta si el token es válido y no ha expirado
        cuenta.setEstado(EstadoCuenta.ACTIVO);
        cuentaRepo.save(cuenta); // Guardar el cambio en la base de datos

        return "Cuenta activada exitosamente.";
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

        Optional<Cuenta> cuentaOptional = cuentaRepo.buscarPorEmail(email);

        if(cuentaOptional.isEmpty()){
            throw new Exception("No existe una cuenta registrada con el email " + email + ".");
        }

        Cuenta cuenta = cuentaOptional.get();

        if(cuenta.getEstado().equals(EstadoCuenta.ELIMINADO)){
            throw new Exception("La cuenta registrada con el email " + email + " esta ELIMINADA.");
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

    private Cuenta obtenerEmail(String correo) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.buscaremail(correo);

        if(cuentaOptional.isEmpty()){
            throw new Exception("El correo dado no está registrado.");
        }

        Cuenta cuenta = cuentaOptional.get();

        if(cuenta.getEstado().equals(EstadoCuenta.ELIMINADO)){
            throw new Exception("La cuenta registrada con el email " + correo + " esta ELIMINADA.");
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
            throw new Exception("La cuenta registrada con el email " + id + " esta ELIMINADA.");
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
        return cuenta.getBoletas().stream()
                .filter(boleta -> boleta.getIdClientepropietario().equals(idPropietario))
                .collect(Collectors.toList());
    }

    @Override
    public Boleta obtenerDetalleBoleta(String idBoleta, String idPropietario) throws Exception {
        Optional<Cuenta> cuenta = cuentaRepo.findById(idPropietario);
        if(cuenta.isPresent()){
            return cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getId().equals(idBoleta))
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
        return cuenta.getBoletas().stream()
                .filter(boleta -> boleta.getIdClientepropietario().equals(idPropietario) && boleta.getEstado() == EstadoBoleta.PENDIENTE)
                .collect(Collectors.toList());
    }

    @Override
    public void transferirBoleta(String idBoleta, String idPropietario, String idNuevoPropietario) throws Exception {

        Optional<Cuenta> cuenta = cuentaRepo.findById(idPropietario);
        Optional<Cuenta> cuenta2 = cuentaRepo.findById(idNuevoPropietario);
        if(cuenta.isPresent() && cuenta2.isPresent()){
            Boleta boletaTra = cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getId().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
            if(boletaTra != null){
                boletaTra.setEstado(EstadoBoleta.ENVIADA);
                cuentaRepo.save(cuenta.get());

                boletaTra.setEstado(EstadoBoleta.PENDIENTE);

                cuenta2.get().getBoletas().add(boletaTra);
                cuentaRepo.save(cuenta2.get());
            }
        }

    }

    @Override
    public void aceptarBoleta(String idBoleta, String idNuevoPropietario) throws Exception {

        Optional<Cuenta> cuenta = cuentaRepo.findById(idNuevoPropietario);
        if(cuenta.isPresent()){
            Boleta boletaTra = cuenta.get().getBoletas().stream()
                    .filter(boleta -> boleta.getId().equals(idBoleta))
                    .findFirst()
                    .orElse(null);
            if(boletaTra != null){
                boletaTra.setEstado(EstadoBoleta.ACEPTADA);
                cuentaRepo.save(cuenta.get());

            }
        }

    }



    private Boleta buscarBoletaPorId(String idBoleta) throws Exception {
        // Lógica para buscar la boleta en las cuentas
        Optional<Cuenta> cuentaOptional = cuentaRepo.findAll().stream()
                .filter(cuenta -> cuenta.getBoletas().stream().anyMatch(boleta -> boleta.getId().equals(idBoleta)))
                .findFirst();

        if (cuentaOptional.isPresent()) {
            return cuentaOptional.get().getBoletas().stream()
                    .filter(boleta -> boleta.getId().equals(idBoleta))
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

        cuenta.get().getPreferencias().addAll(tipoPreferencias);
        cuentaRepo.save(cuenta.get());
    }
    @Override
    public List<TipoEvento> obtenerPreferenciasUsuario(String idUsuario) throws Exception {
        // Verificar si el usuario existe en la base de datos
        Cuenta usuario = cuentaRepo.findById(idUsuario)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Devolver las preferencias del usuario
        return usuario.getPreferencias();
    }





}
