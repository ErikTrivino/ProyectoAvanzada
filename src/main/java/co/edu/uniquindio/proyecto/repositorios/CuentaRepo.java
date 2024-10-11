package co.edu.uniquindio.proyecto.repositorios;

import co.edu.uniquindio.proyecto.modelo.documentos.Cuenta;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {

    @Query("{email : email}")
    Optional<Cuenta> buscaremail(String email);

    @Query("{usuario.cedula : cedula}")
    Optional<Cuenta> buscarCedula(String cedula);

    @Query("{email: correo, password: password }")
    Optional<Cuenta> validarDatosAutenticacion(String correo, String password);

    @Query("{email: email}")
    Optional<Cuenta> buscarPorEmail(String email);
}
