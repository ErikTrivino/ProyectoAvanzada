package co.edu.uniquindio.proyecto.servicios.implementaciones;

import co.edu.uniquindio.proyecto.servicios.interfaces.ImagenesServicio;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ImagenesServicioImpl implements ImagenesServicio {

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    private Cloudinary cloudinary;

    @PostConstruct
    public void initCloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
    }

    @Override
    public Map<String, String> subirImagen(MultipartFile imagen) throws Exception {
        if (imagen.isEmpty()) {
            throw new IllegalArgumentException("La imagen está vacía");
        }

        File file = convertir(imagen);
        Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.asMap("folder", "proyecto"));

        Map<String, String> resultado = new HashMap<>();
        resultado.put("url", (String) uploadResult.get("secure_url"));
        resultado.put("public_id", (String) uploadResult.get("public_id"));
        return resultado;
    }

    @Override
    public Map eliminarImagen(String idImagen) throws Exception {
        Map resultado = cloudinary.uploader().destroy(idImagen, ObjectUtils.emptyMap());
        if (!"ok".equals(resultado.get("result"))) {
            throw new RuntimeException("No se pudo eliminar la imagen o no existía: " + idImagen);
        }
        return resultado;
    }

    private File convertir(MultipartFile imagen) throws IOException {
        String originalName = imagen.getOriginalFilename();
        String extension = originalName != null && originalName.contains(".") ? originalName.substring(originalName.lastIndexOf('.')) : ".tmp";

        File file = File.createTempFile(UUID.randomUUID().toString(), extension);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(imagen.getBytes());
        }
        return file;
    }
}
