package com.babystore.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    JavaMailSender javaMailSender;

    private static final String emailPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    //
    // public boolean emailRegex(String email) {
    // return Pattern.compile(emailPattern).matcher(email).matches();
    // }

    public String emailTemplate(String name, String link) {
        return "<h3> Hi " + name + ", </h3>" +
                "<p>Thanks for getting started with our [customer portal]!</p>" + "</br>" +
                "<p>We need a little more information to complete your registration,</p>" +
                "<p>including a confirmation of your email address.</p>" + "</br>" +
                "<p>Click below to confirm your email address:</p>" + "</br>" +
                "<a href=\"" + link + "\">" + link + "</a>" + "</br>" +
                "<p>If you have problems, please paste the above URL into your web browser.</p>";
    }

}
