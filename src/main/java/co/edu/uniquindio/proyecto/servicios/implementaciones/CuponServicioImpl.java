package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.modelo.documentos.Cupon;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.CrearCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.EditarCuponDTO;
import co.edu.uniquindio.proyecto.modelo.dto.cupon.InformacionCuponDTO;
import co.edu.uniquindio.proyecto.modelo.enums.EstadoCupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;
import co.edu.uniquindio.proyecto.repositorios.CuponRepo;
import co.edu.uniquindio.proyecto.servicios.interfaces.CuponServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class CuponServicioImpl implements CuponServicio {

    private final CuponRepo cuponRepo;

    @Override
    public List<Cupon> listarCuponesPorTipo(TipoCupon tipo) throws Exception {
        return cuponRepo.buscarCuponesPorTipo(tipo);
    }

    @Override
    public Cupon crearCupon(CrearCuponDTO cuponDTO) throws Exception {
        // Validar si ya existe un cupón con el mismo código
        if (existeCodigo(cuponDTO.codigo())) {
            throw new Exception("Ya existe un cupón con el código " + cuponDTO.codigo());
        }

        Cupon nuevoCupon = new Cupon();
        nuevoCupon.setCodigo(cuponDTO.codigo());
        nuevoCupon.setDescuento(cuponDTO.descuento());
        nuevoCupon.setFechaVencimiento(cuponDTO.fechaVencimiento());
        nuevoCupon.setNombre(cuponDTO.nombre());
        nuevoCupon.setEstado(cuponDTO.estado());
        nuevoCupon.setTipo(cuponDTO.tipo());

        cuponRepo.save(nuevoCupon);
        return nuevoCupon;
    }

    @Override
    public Cupon actualizarCupon(EditarCuponDTO cuponDTO) throws Exception {
        Cupon cuponExistente = obtenerCupon(cuponDTO.id());

        cuponExistente.setNombre(cuponDTO.nombre());
        cuponExistente.setDescuento(cuponDTO.descuento());
        cuponExistente.setFechaVencimiento(cuponDTO.fechaVencimiento());
        cuponExistente.setEstado(cuponDTO.estado());
        cuponExistente.setTipo(cuponDTO.tipo());

        cuponRepo.save(cuponExistente);
        return cuponExistente;
    }

    @Override
    public void eliminarCupon(String id) throws Exception {
        Cupon cupon = obtenerCupon(id);
        cuponRepo.delete(cupon);
    }

    @Override
    @Transactional(readOnly = true)
    public InformacionCuponDTO obtenerInformacionCupon(String id) throws Exception {
        Cupon cupon = obtenerCupon(id);

        return new InformacionCuponDTO(
                cupon.getId(),
                cupon.getNombre(),
                cupon.getDescuento(),
                cupon.getFechaVencimiento(),
                cupon.getCodigo(),
                cupon.getEstado(),
                cupon.getTipo()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cupon> listarCuponesActivos() throws Exception {
        return cuponRepo.buscarCuponesActivos();
    }

    @Override
    public boolean redimirCupon(String codigo) throws Exception {
        Cupon cupon = obtenerPorCodigo(codigo);

        if (cupon.getFechaVencimiento().isBefore(LocalDateTime.now())) {
            throw new Exception("El cupón ha vencido.");
        }

        cupon.setEstado(EstadoCupon.INACTIVO);
        cuponRepo.save(cupon);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cupon> listarCuponesPorExpirar() throws Exception {
        LocalDateTime fechaLimite = LocalDateTime.now().plusDays(7);
        Date fechaLimiteDate = Date.from(fechaLimite.atZone(ZoneId.systemDefault()).toInstant());

        return cuponRepo.buscarCuponesPorExpirarAntesDe(fechaLimiteDate);
    }

    // Métodos privados de utilidad

    private boolean existeCodigo(String codigo) {
        return cuponRepo.buscarPorCodigo(codigo).isPresent();
    }

    private Cupon obtenerCupon(String id) throws Exception {
        Optional<Cupon> cuponOptional = cuponRepo.findById(id);

        if (cuponOptional.isEmpty()) {
            throw new Exception("No existe un cupón con el id " + id);
        }

        return cuponOptional.get();
    }

    private Cupon obtenerPorCodigo(String codigo) throws Exception {
        Optional<Cupon> cuponOptional = cuponRepo.buscarPorCodigo(codigo);

        if (cuponOptional.isEmpty()) {
            throw new Exception("No existe un cupón con el código " + codigo);
        }

        return cuponOptional.get();
    }
}
