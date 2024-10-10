package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Carrito;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoRepo extends MongoRepository<Carrito, String> {

    // Buscar un carrito por el idUsuario
    @Query("{ 'idUsuario' : ?0 }")
    Optional<Carrito> buscarCarritoPorIdUsuario(String idUsuario);

    // Buscar un carrito por el ID del carrito
    @Query("{ '_id' : ?0 }")
    Optional<Carrito> buscarCarritoPorId(String idCarrito);

    // Buscar todos los carritos que contienen un evento específico (por idEvento)
    @Query("{ 'items.idEvento' : ?0 }")
    List<Carrito> buscarCarritosPorEvento(ObjectId idEvento);

    // Buscar todos los carritos con más de X cantidad de items
    @Query("{ 'items.cantidad' : { $gt: ?0 } }")
    List<Carrito> buscarCarritosConMasDeXItems(int cantidad);


}

