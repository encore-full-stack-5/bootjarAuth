package com.example.bootjarAuth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender emailSender;

    // Email
    @Override
    public void sendEmail(String address, byte[] qrCode) throws IOException, MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        // 보내는 이
        helper.setFrom(String.valueOf(new InternetAddress("parkmyurm@gmail.com", "YOU&I✨TODO")));
        // 받는 이
        helper.setTo(address);
        // 제목
        helper.setSubject("[YOU&I TODO] 비밀번호 변경 QR코드 입니다.");
        // 내용
        helper.setText("QR코드로 접속 후 비밀번호를 변경해 주세요.\n(5분 뒤 만료됩니다.)");
        // QR Code
        helper.addAttachment("QRCode.png", new ByteArrayResource(qrCode), "image/png");

        emailSender.send(message);
    }
}
