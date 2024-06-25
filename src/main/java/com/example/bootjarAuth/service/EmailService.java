package com.example.bootjarAuth.service;

import jakarta.mail.MessagingException;

import java.io.IOException;

public interface EmailService {
    // Email
    void sendEmail(String address, byte[] qrCode) throws IOException, MessagingException;
}
