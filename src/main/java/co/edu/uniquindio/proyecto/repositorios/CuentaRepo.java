package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {

    @Query("{ 'email' : ?0 }")
    Optional<Cuenta> buscaremail(String email);

    @Query("{ 'usuario.cedula' : ?0 }")
    Optional<Cuenta> buscarCedula(String cedula);

    @Query("{email: ?0, password: ?1 }")
    Optional<Cuenta> validarDatosAutenticacion(String correo, String password);

    @Query("{'email': ?0}")
    Optional<Cuenta> buscarPorEmail(String email);


    Optional<Cuenta> findByEmail(String email);

    @Query("{ 'codigoValidacionRegistro.codigo': ?0 }")
    Optional<Cuenta> buscarPorCodigoValidacion(String token);


}
