package co.edu.uniquindio.proyecto.servicios.interfaces;

import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import co.edu.uniquindio.proyecto.modelo.vo.Boleta;
import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
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
    Boleta obtenerDetalleBoleta(String idBoleta, String idPropietario) throws Exception;
    List<Boleta> listarBoletasEnviadas(String idPropietario) throws Exception;
    List<Boleta> listarBoletasPendientes(String idPropietario) throws Exception;
    void transferirBoleta(String idBoleta, String idPropietario, String idNuevoPropietario) throws Exception;
    void aceptarBoleta(String idBoleta, String idNuevoPropietario) throws Exception;

    List<TipoEvento> obtenerPreferencias() throws  Exception;

    void agregarPreferenciasUsuario(String idUsuario, List<TipoEvento> tipoPreferencias) throws  Exception;

    List<TipoEvento> obtenerPreferenciasUsuario(String idUsuario) throws Exception;

    List<ItemCuentaDTO> listarCuentas() throws Exception;

    Cuenta obtenerPorEmail(String email) throws Exception;

    String enviarCodigoActivacionCuenta(String correo) throws Exception;
}
