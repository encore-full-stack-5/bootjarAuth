package com.example.bootjarAuth.service;

import com.example.bootjarAuth.dto.Response.TokenResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class QRCodeServiceImpl implements QRCodeService {
    private final JwtUtil jwtUtil;

    // QR Code
    @Override
    public TokenResponse generateQRToken(String email) {
        String token = jwtUtil.generateQRToken(email);
        return TokenResponse.from(token);
    }
    @Override
    public byte[] generateQRCodeImage(String email, String changePasswordUrl) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>(); // 코드 설정 지정
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // UTF-8로 인코딩
        // 오류 정정 레벨을 낮은 수준으로 지정 (높을 수록 QR코드 내부 데이터 복구 능력이 강화됨)
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.MARGIN, 1); // QR코드 주변의 여백

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // (url, 바코드 형식.QR_CODE, 코드 너비, 코드 높이, hints)
        BitMatrix bitMatrix = qrCodeWriter.encode(changePasswordUrl, BarcodeFormat.QR_CODE, 300, 300, hints);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix); // 이미지로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // byte배열로 반환
        ImageIO.write(image, "png", baos); // (이미지, 포맷, 출력)
        return baos.toByteArray();
    }
}
