package es.upm.etsisi.qhcube.config;

import es.upm.etsisi.qhcube.utils.EnvUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {

    private final EnvUtil envUtil;

    public MailSenderConfig(EnvUtil envUtil) {
        this.envUtil = envUtil;
    }


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(envUtil.getMailHost());
        mailSender.setPort(envUtil.getMailPort());
        mailSender.setUsername(envUtil.getMailUsername());
        mailSender.setPassword(envUtil.getMailPassword());
        mailSender.setDefaultEncoding(envUtil.getMailEncoding());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", envUtil.getMailTrasnportProtocol());
        props.put("mail.smtp.auth", envUtil.isMailSmtpAuth());
        props.put("mail.smtp.starttls.enable", envUtil.isMailSmtpStarttlsEnable());
        props.put("mail.debug", false);

        return mailSender;
    }
}
