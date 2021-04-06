package com.c2g4.SingHealthWebApp.Notifications;

import com.c2g4.SingHealthWebApp.Admin.Report.ReportBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service("EmailService")
public class EmailServiceImpl {

    @Value("${spring.mail.username}")
    private String mailServerUsername;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SimpleMailMessage template;

    private static final Logger logger = LoggerFactory.getLogger(ReportBuilder.class);

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            System.out.println("sending message to" +to);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailServerUsername);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            emailSender.send(message);
        } catch (MailException exception) {
            exception.printStackTrace();
        }
    }

    public void sendSimpleMessageUsingTemplate(String to,
                                               String subject,
                                               String ...templateModel) {
        String text = String.format(template.getText(), templateModel);
        sendSimpleMessage(to, subject, text);
    }

    public void sendMessageWithAttachment(String to,
                                          String subject,
                                          String text,
                                          String pathToAttachment) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            // pass 'true' to the constructor to create a multipart message
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(mailServerUsername);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
            helper.addAttachment("Invoice", file);

            emailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}