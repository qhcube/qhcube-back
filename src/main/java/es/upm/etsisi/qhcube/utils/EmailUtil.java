package es.upm.etsisi.qhcube.utils;

import es.upm.etsisi.qhcube.config.MailSenderConfig;
import es.upm.etsisi.qhcube.dto.PlayerDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

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

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(player.getEmail());
        helper.setSubject(subject);
        helper.setReplyTo(fromAddress, senderName);

        String verifyURL = this.envUtil.getServerUrl()
            + "/email-validation?code=" + player.getVericode()
            + "&email=" + player.getEmail();

        helper.setText(buildVerificationContent(player, verifyURL, envUtil.getUnverifiedAccountsMaxDays()), true);

        emailSender.send(message);
    }

    static String buildVerificationContent(PlayerDTO player, String verifyURL, long maxDays) {
        String fullName = HtmlUtils.htmlEscape(player.getNombre() + " " + player.getApellidos());

        String content = "Estimado <b>" + fullName + "</b>,<br/><br/>"
            + "Haz click en el enlace de abajo para verificar tu registro:<br>"
            + "<h3><a href=\"[[URL]]\" target='_self'>VERIFICAR</a></h3><br/>"
            + "También puedes copiar y pegar el siguiente enlace en tu navegador:<br/><br/>"
            + "[[URL]]<br/><br/>"
            + "Tienes " + maxDays + " días desde el registro para activar tu cuenta. "
            + "Pasado ese plazo, se borrarán tus datos y tendrás que registrarte de nuevo.<br/><br/><br/>"
            + "¡GRACIAS!<br/><br/>"
            + "QHCube by Jesús Lacalle.";

        content = content.replace("[[URL]]", verifyURL);

        return content;
    }
}
