package co.edu.uniquindio.proyecto.servicios.implementaciones;


import co.edu.uniquindio.proyecto.modelo.documentos.Orden;
import co.edu.uniquindio.proyecto.modelo.dto.email.EmailDTO;
import co.edu.uniquindio.proyecto.servicios.interfaces.EmailServicio;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.util.ByteArrayDataSource;
import org.simplejavamail.api.email.AttachmentResource;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Service
public class EmailServicioImpl implements EmailServicio {

    @Override
    @Async
    public void enviarCorreo(EmailDTO emailDTO) throws Exception {


        Email email = EmailBuilder.startingBlank()
                .from("unieventosfae@gmail.com")
                .to(emailDTO.destinatario())
                .withSubject(emailDTO.asunto())
                .withPlainText(emailDTO.cuerpo())
                .buildEmail();

        //unieventosfae@gmail.com
        //fae12345
        //clave de aplicación: yygy ngcd lulw oxjk
        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "unieventosfae@gmail.com", "yygy ngcd lulw oxjk")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {

            mailer.sendMail(email);
        }


    }

    @Override
    @Async
    public void enviarCorreoConQr(EmailDTO emailDTO, Orden orden) throws Exception {
        // Generar contenido del QR
        String contenidoQr = generarContenidoQr(orden);

        // Generar la imagen del código QR
        ByteArrayOutputStream qrStream = new ByteArrayOutputStream();
        generarImagenQr(contenidoQr, qrStream);

        // Crear el correo con el adjunto del QR
        Email email = EmailBuilder.startingBlank()
                .from("unieventosfae@gmail.com")
                .to(emailDTO.destinatario())
                .withSubject(emailDTO.asunto())
                .withPlainText(emailDTO.cuerpo())
                .withAttachment("codigo_qr.png", qrStream.toByteArray(), "image/png")
                .buildEmail();

        // Enviar el correo
        try (Mailer mailer = MailerBuilder
                .withSMTPServer("smtp.gmail.com", 587, "unieventosfae@gmail.com", "yygy ngcd lulw oxjk")
                .withTransportStrategy(TransportStrategy.SMTP_TLS)
                .withDebugLogging(true)
                .buildMailer()) {
            mailer.sendMail(email);
        }


    }

    // Método para generar el contenido del QR a partir de la orden
    private String generarContenidoQr(Orden orden) {
        return "Orden ID: " + orden.getId() + "\n" +
                "Cliente ID: " + orden.getIdCliente() + "\n" +
                "Fecha: " + orden.getFecha() + "\n" +
                "Total: $" + orden.getTotal();
    }

    // Método para generar la imagen del QR
    private void generarImagenQr(String contenido, ByteArrayOutputStream outputStream) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(contenido, BarcodeFormat.QR_CODE, 300, 300, hints);
        BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ImageIO.write(qrImage, "png", outputStream);
    }


}
