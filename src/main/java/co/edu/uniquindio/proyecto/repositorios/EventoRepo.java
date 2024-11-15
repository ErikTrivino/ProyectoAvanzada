package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import co.edu.uniquindio.proyecto.modelo.enums.TipoEvento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepo extends MongoRepository<Evento, String> {

    @Query("{ 'tipo' : { $in: ?0 }, 'estado': 'ACTIVO' }")
    List<Evento> filtrarEventosPorTipos(List<TipoEvento> tipos);
    @Query("{nombre : ?0, fechaEvento: ?1, ciudad :  ?2}")
    Optional<Evento> buscarEvento(String nombreEvento, LocalDate fechaEvento, String ciudad);

    @Query("{nombre : ?0, tipo: ?1, ciudad :  ?2, estado: ACTIVO}")
    List<Evento> filtrarEventos(String nombreEvento, TipoEvento tipo, String ciudad);

    @Query("{estado: ACTIVO}")
    List<Evento> listarEventosActivos();

    @Query("{fecha: {$gte: ?0, $lte: ?1, estado : ACTIVO}}")
    List<Evento> filtrarEventosFuturos(LocalDateTime fechaInicio, LocalDateTime fechaFin);


}
