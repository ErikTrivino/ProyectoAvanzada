package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdenRepo extends MongoRepository<Orden, String> {

    @Query("{id : id}")
    Optional<Orden> buscarOrden(String id);
    @Query("{ 'idCliente' : ?0 }")
    List<Orden> buscarOrdenesPorCliente(String idCliente);


    @Query("{ 'fecha' : { $gte: ?0, $lte: ?1 } }")
    List<Orden> buscarOrdenesPorRangoDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);


}
