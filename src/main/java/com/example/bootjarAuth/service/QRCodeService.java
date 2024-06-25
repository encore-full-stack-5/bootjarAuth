package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.Response.TokenResponse;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface QRCodeService {
    // QR Code
    TokenResponse generateQRToken(String email);
    byte[] generateQRCodeImage(String email, String changePasswordUrl) throws WriterException, IOException;
}
