package co.edu.uniquindio.proyecto.test;
import co.edu.uniquindio.proyecto.servicios.implementaciones.ImagenesServicioImpl;
import co.edu.uniquindio.proyecto.servicios.interfaces.ImagenesServicio;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ImagenServicioTest {

    @Autowired
    private ImagenesServicio imagenesServicio; ;

    @Test
    public void testSubirImagen() throws Exception {
        // Cargar la imagen desde src/test/resources
        File imageFile = new File("src/test/resources/gatoImagen.jpg"); // Ajusta el nombre y la ruta según sea necesario

        // Crear un MockMultipartFile a partir del archivo
        MultipartFile multipartFile = new MockMultipartFile(
                "testImage.jpg",  // Nombre del archivo en la solicitud
                imageFile.getName(),  // Nombre original del archivo
                "image/jpeg",  // Tipo de contenido
                Files.readAllBytes(imageFile.toPath())  // Contenido del archivo
        );

        // Llamar al método subirImagen
        String url = imagenesServicio.subirImagen(multipartFile);

        // Verificar que la URL no sea nula
        assertNotNull(url);
        System.out.println(url);
        assertTrue(url.contains("https://firebasestorage.googleapis.com/v0/b/"));
    }

    @Test
    public void eliminarImagenTest() throws Exception {

        String imagen = "8d6964e2-c03f-4d8d-832d-2befdebce4f9-gatoImagen.jpg";

        imagenesServicio.eliminarImagen(imagen);

        // Verificar que la imagen se haya eliminado
        // Intentar obtener el Blob después de eliminarla
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get(imagen);

        // Verificar que el blob es null, lo que indica que la imagen fue eliminada
        assertNull(blob);
    }
}



