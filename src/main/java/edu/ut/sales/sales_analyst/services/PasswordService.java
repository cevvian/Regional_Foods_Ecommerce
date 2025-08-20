package edu.ut.sales.sales_analyst.services;

import edu.ut.sales.sales_analyst.model.dtos.responses.OTP;
import edu.ut.sales.sales_analyst.model.entities.User;
import edu.ut.sales.sales_analyst.repositories.UserRepo;
import edu.ut.sales.sales_analyst.services.impl.IPasswordService;
import jakarta.mail.MessagingException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PasswordService implements IPasswordService {

    private final UserRepo userRepository;
    private final EmailService emailService;
    private final Map<String, OTP> otpStorage = new ConcurrentHashMap<>();

    public PasswordService(UserRepo userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public void verifyEmailAndSendOTP(String email) throws MessagingException, IOException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(email);
        }

        String otp = otpGenerator(6);

        OTP otpData = new OTP(otp, new Date(System.currentTimeMillis() + 60 * 1000));
        otpStorage.put(email, otpData);
        emailService.sendOTPEmail(email, otpData);
    }

    @Override
    public void verifyOTP(String email, Integer otp) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm tháy người dùng có email: " + email);
        }

        OTP otpData = otpStorage.get(email);
        if (otpData != null) {
            if (otpData.getOtp().equals(otp.toString())) {
                if (otpData.getExpirationTime().before(new Date())) {
                    throw new RuntimeException("Mã OTP đã hết hạn!");
                }

                otpStorage.remove(email);
            } else {
                throw new RuntimeException("Mã OTP không hợp lệ cho email: " + email);
            }
        } else {
            throw new RuntimeException("Không tìm thấy OTP hợp lệ cho email: " + email);
        }

    }

    private String otpGenerator(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }
}
