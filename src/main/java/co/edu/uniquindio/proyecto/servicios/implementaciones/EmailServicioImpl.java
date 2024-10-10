package co.edu.uniquindio.proyecto.servicios.implementaciones;


import co.edu.uniquindio.proyecto.modelo.dto.email.EmailDTO;
import co.edu.uniquindio.proyecto.servicios.interfaces.EmailServicio;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


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


}
