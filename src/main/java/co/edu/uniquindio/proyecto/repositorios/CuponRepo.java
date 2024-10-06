package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cupon;
import co.edu.uniquindio.proyecto.modelo.enums.TipoCupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CuponRepo extends MongoRepository<Cupon, String> {

    @Query("{codigo : ?0 }")
    Optional<Cupon> buscarPorCodigo(String codigo);

    @Query("{estado : ACTIVO}")
    List<Cupon> buscarCuponesActivos();

    @Query("{fechaVencimiento: { $lt: ?0 }}")
    List<Cupon> buscarCuponesPorExpirarAntesDe(java.util.Date fecha);

    @Query("{ 'tipo' : ?0 }")
    List<Cupon> buscarCuponesPorTipo(TipoCupon tipo);


}
