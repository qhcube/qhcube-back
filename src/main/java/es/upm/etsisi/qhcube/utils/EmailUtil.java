package es.upm.etsisi.qhcube.utils;

import es.upm.etsisi.qhcube.config.MailSenderConfig;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import java.io.UnsupportedEncodingException;

@Slf4j
@Component
public class EmailUtil {

    private final EnvUtil envUtil;
    private final MailSenderConfig mailSenderConfig;

    public EmailUtil(EnvUtil envUtil, MailSenderConfig mailSenderConfig) {
        this.envUtil = envUtil;
        this.mailSenderConfig = mailSenderConfig;
    }

    public void sendVerificationEmail(PlayerDTO player)
            throws MessagingException, UnsupportedEncodingException {

        JavaMailSender emailSender = this.mailSenderConfig.getJavaMailSender();

        String fromAddress = envUtil.getMailFromAddress();
        String senderName = envUtil.getMailSenderName();
        String subject = "Por favor, verifica tu registro";

        String content = "Estimado " + player.getNombre() + " " + player.getApellidos() + ",<br>"
                + "Haz click al enlace de abajo para verificar tu registro:<br>"
                + "<h3><a href=\"[[URL]]\" target='_self'>VERIFICAR</a></h3>"
                + "¡GRACIAS!<br />"
                + "QHCube Juego by Jesús García.";

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(player.getEmail());
        helper.setSubject(subject);
        helper.setReplyTo(fromAddress, senderName);

        String verifyURL = "http://" + this.envUtil.getServerHost() + ":" + this.envUtil.getServerPort()
                + "/verify?code=" + player.getVericode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        emailSender.send(message);
    }
}
