package co.edu.uniquindio.proyecto.servicios.interfaces;

import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.dto.autenticacion.TokenDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cuenta.*;

import java.util.List;

public interface CuentaServicio {

    String crearCuenta(CrearCuentaDTO cuenta) throws Exception;

    String editarCuenta(EditarCuentaDTO cuenta) throws Exception;

    String eliminarCuenta(String id) throws Exception;

    InformacionCuentaDTO obtenerInformacionCuenta(String id) throws Exception;

    String enviarCodigoRecuperacionPassword(String correo) throws Exception;

    String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO ) throws Exception;

    TokenDTO iniciarSesion(LoginDTO loginDTO) throws Exception;

    String activarCuenta(ActivarCuentaDTO activarCuentaDTO) throws Exception;


    ///NUEVA FUNCIONALIDAD
    List<Boleta> buscarBoletasPorPropietario(String idPropietario) throws Exception;
    Boleta obtenerDetalleBoleta(String idBoleta) throws Exception;
    List<Boleta> listarBoletasEnviadas(String idPropietario) throws Exception;
    List<Boleta> listarBoletasPendientes(String idPropietario) throws Exception;
    void transferirBoleta(String idBoleta, String nuevoPropietario, String correo) throws Exception;
    void aceptarBoleta(String idBoleta) throws Exception;

    List<TipoEvento> obtenerPreferencias() throws  Exception;

    void agregarPreferenciasUsuario(String idUsuario, List<TipoEvento> tipoPreferencias) throws  Exception;
}
