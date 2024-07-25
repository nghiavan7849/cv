package com.snackviet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
	@Autowired
	JavaMailSender sender;
	public boolean sendOtp(String to, String subjec, String content) {
		MimeMessage mimeMessage = sender.createMimeMessage();	
		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "utf-8");
			mimeMessageHelper.setFrom("tranvn7849@gmail.com");
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setSubject(subjec);
			mimeMessageHelper.setText(content);
			sender.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
	}
}
