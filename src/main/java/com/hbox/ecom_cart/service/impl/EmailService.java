package com.hbox.ecom_cart.service.impl;

import com.hbox.ecom_cart.dto.EmailDto;
import jakarta.mail.MessageRemovedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(EmailDto emailDto) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(emailDto.getTo());
        helper.setSubject(emailDto.getSubject());
        helper.setText(emailDto.getBody(), true);
        mailSender.send(mimeMessage);
    }
}
