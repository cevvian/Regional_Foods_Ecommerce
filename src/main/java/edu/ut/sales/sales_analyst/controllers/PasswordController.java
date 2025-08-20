package edu.ut.sales.sales_analyst.controllers;

import edu.ut.sales.sales_analyst.model.dtos.requests.ForgotPasswordRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.ResetPasswordRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.SendOtpRequest;
import edu.ut.sales.sales_analyst.model.dtos.requests.VerifyOtpRequest;
import edu.ut.sales.sales_analyst.model.dtos.responses.ResponseAPI;
import edu.ut.sales.sales_analyst.services.PasswordService;
import edu.ut.sales.sales_analyst.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/passwords")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Passwords", description = "APIs for reset password, forgot password")
public class PasswordController {

    PasswordService passwordService;
    UserService userService;

    @PostMapping("/send-otp")
    @Operation(summary = "Send OTP to email")
    public ResponseEntity<String> sendOtp(@RequestBody SendOtpRequest request)
            throws MessagingException, IOException {
        passwordService.verifyEmailAndSendOTP(request.getEmail());
        return ResponseEntity.ok("OTP đã được gửi tới email: " + request.getEmail());
    }

    @PostMapping("/verify-otp")
    @Operation(summary = "Verify OTP code sent to email")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequest request) {
        passwordService.verifyOTP(request.getEmail(), request.getOtp());
        return ResponseEntity.ok("OTP hợp lệ, bạn có thể đặt lại mật khẩu mới.");
    }

    @Operation(summary = "Reset user password")
    @PostMapping("/reset-password")
    public ResponseEntity<ResponseAPI<Boolean>> resetPassword(
            @RequestBody ResetPasswordRequest request) {

        Boolean result = userService.resetPassword(request);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI<>("Password reset failed", HttpStatus.BAD_REQUEST, false));
        }

        return ResponseEntity
                .ok(new ResponseAPI<>("Password reset successfully", HttpStatus.OK, true));
    }

    @Operation(summary = "Forgot password")
    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseAPI<Boolean>> forgotPassword(
            @RequestBody ForgotPasswordRequest request) {

        Boolean result = userService.forgetPassword(request);

        if (!result) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseAPI<>("Password reset failed", HttpStatus.BAD_REQUEST, false));
        }

        return ResponseEntity
                .ok(new ResponseAPI<>("Password updated successfully", HttpStatus.OK, true));
    }
}
