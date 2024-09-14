package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import co.edu.uniquindio.proyecto.modelo.documentos.Evento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EventoRepo extends MongoRepository<Evento, String> {

    @Query("{nombre : nombreEvento, fechaEvento: fechaEvento, ciudad :  ciudad}")
    Optional<Evento> buscarEvento(String nombreEvento, LocalDateTime fechaEvento, String ciudad);
}
