package com.example.bootjarAuth.service;

import com.example.bootjarAuth.domain.AuthRepository;
import com.example.bootjarAuth.domain.User;
import com.example.bootjarAuth.dto.*;
import com.example.bootjarAuth.dto.Request.LoginRequest;
import com.example.bootjarAuth.dto.Request.SignUpRequest;
import com.example.bootjarAuth.dto.Response.LoginResponse;
import com.example.bootjarAuth.dto.Response.SearchResponse;
import com.example.bootjarAuth.dto.Response.UserResponse;
import com.example.bootjarAuth.global.utils.JwtUtil;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final GcsService gcsService;
    private final JavaMailSender emailSender;

    @Override
    public void signUp(SignUpRequest signUpRequest) {
        //이메일 중복 제한
        if(authRepository.existsByEmail(signUpRequest.getEmail()))
            throw new IllegalArgumentException("중복된 이메일입니다.");
        //닉네임 중복 제한
        if(authRepository.existsByNickname(signUpRequest.getNickname()))
            throw new IllegalArgumentException("중복된 닉네임입니다.");

        String encodedPassword = passwordEncoder.encode(signUpRequest.getPassword());
        authRepository.save(signUpRequest.toEntity(encodedPassword));
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        User byEmail = authRepository.findByEmail(loginRequest.getEmail());
        if(byEmail == null ||
                !passwordEncoder.matches(loginRequest.getPassword(), byEmail.getPassword()))
            throw new IllegalArgumentException("Email 혹은 비밀번호가 틀렸습니다.");

        String token = jwtUtil.generateToken(byEmail.getId(), loginRequest.getEmail());
        return LoginResponse.from(token);
    }

    @Override
    public UserResponse getUser(String token) {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");

        return  UserResponse.from(user);
    }

    @Transactional
    @Override
    public void deleteUser(String token) {
        authRepository.deleteByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
    }

    @Transactional
    @Override
    public void updateUser(String token, UpdateDto updateDto) throws IOException {

        User user = authRepository.findByEmail(jwtUtil.getByEmailFromTokenAndValidate(token));
        if(user == null) throw  new IllegalArgumentException("해당하는 회원이 없습니다");

        String imageUrl = gcsService.uploadFile(updateDto.getImage().getOriginalFilename(), updateDto.getImage().getBytes());


        user.setNickname(updateDto.getNickname());
        if (updateDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        user.setImage(imageUrl);

    }

    @Override
    public List<SearchResponse> searchUser(String nickname) {
       return authRepository.findByNicknameContaining(nickname).stream().map(SearchResponse::from).toList();
    }

    // QR Code
    @Override
    public byte[] generateQRCodeImage(String changePasswordUrl) throws WriterException, IOException {
        Map<EncodeHintType, Object> hints = new HashMap<>(); // 코드 설정 지정
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8"); // UTF-8로 인코딩
        // 오류 정정 레벨을 낮은 수준으로 지정 (높을 수록 QR코드 내부 데이터 보구 능력이 강화됨
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 1); // QR코드 주변의 여백

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        // (url, 바코드 형식.QR_CODE, 코드 너비, 코드 높이, hints)
        BitMatrix bitMatrix = qrCodeWriter.encode(changePasswordUrl, BarcodeFormat.QR_CODE, 300, 300, hints);

        BufferedImage image = MatrixToImageWriter.toBufferedImage(bitMatrix); // 이미지로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // byte배열로 반환
        ImageIO.write(image, "png", baos); // (이미지, 포맷, 출력)
        return baos.toByteArray();
    }

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