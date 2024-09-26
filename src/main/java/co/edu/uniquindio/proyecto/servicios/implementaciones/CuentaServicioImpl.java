package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.config.JWTUtils;
import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Usuario;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCuenta;
import co.edu.uniquindio.proyecto.modelo.enums.Rol;
import co.edu.uniquindio.proyecto.modelo.vo.CodigoValidacion;
import co.edu.uniquindio.proyecto.repositorios.CuentaRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuentaServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CuentaServicioImpl implements CuentaServicio {

    private final CuentaRepo cuentaRepo;
    private final JWTUtils jwtUtils;
    //private final FutureOrPresentValidatorForLocalDateTime futureOrPresentValidatorForLocalDateTime;


    @Override
    public String crearCuenta(CrearCuentaDTO cuenta) throws Exception {
        //en este metodo se debe validar todo lo que puede fallar, por ejemplo que no se repitan cedulas o correos

        if( existeCorreo(cuenta.correo())){

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
                        LocalDateTime.now(), "HHJ77" //EL CODIGO DEBE SER UNA CADENA GENERADA ALEATORIAMENTE
                ));

        cuentaRepo.save(nuevaCuenta);
        //SE DEBE ENVIAR AL CORREO DEL USUARIO EL CODIGO DE VALIDACIÓN
        return "Su cuenta se ha generado con éxito.";
    }

    @Override
    public String editarCuenta(EditarCuentaDTO cuenta) throws Exception {

        Cuenta cuentaModificada = obtenerCuenta(cuenta.id());

        cuentaModificada.getUsuario().setNombre( cuenta.nombre());
        cuentaModificada.getUsuario().setDireccion( cuenta.direccion());
        cuentaModificada.getUsuario().setTelefono( cuenta.telefono());
        cuentaModificada.setPassword(encriptarPassword(cuenta.password()));

        cuentaRepo.save(cuentaModificada);
        return cuentaModificada.getId();
    }

    @Override
    public String eliminarCuenta(String id) throws Exception {

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
                cuenta.getEmail()
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

        return "Se ha enviado un correo con el código de validación";

    }

    @Override
    public String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {

        Cuenta cuentaOptional = obtenerEmail(cambiarPasswordDTO.email());

        CodigoValidacion codigoValidacion = cuentaOptional.getCodigoValidacionPassword();

        if(codigoValidacion.getCodigo().equals(cambiarPasswordDTO.codigoVerificacion())){
            if(codigoValidacion.getFechaCreacion().plusMinutes(15).isBefore(LocalDateTime.now())){
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
        return "";
    }

    private Cuenta obtenerEmail(String correo) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.buscaremail(correo);

        if(cuentaOptional.isEmpty()){
            throw new Exception("El correo dado no está registrado.");
        }

        return cuentaOptional.get();

    }

    private Cuenta obtenerCuenta(String id) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.findById(id);

        if(cuentaOptional.isEmpty()){
            throw new Exception("No existe una cuenta registrada con el id " + id + ".");
        }

        return cuentaOptional.get();

    }

    private Cuenta obtenerPorEmail(String email) throws Exception {

        Optional<Cuenta> cuentaOptional = cuentaRepo.buscarPorEmail(email);

        if(cuentaOptional.isEmpty()){
            throw new Exception("No existe una cuenta registrada con el email " + email + ".");
        }

        return cuentaOptional.get();

    }

    private boolean existeCedula(String cedula) {
        return cuentaRepo.buscarCedula(cedula).isPresent();
    }

    private boolean existeCorreo(String correo) {

        return cuentaRepo.buscaremail(correo).isPresent();

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


}
