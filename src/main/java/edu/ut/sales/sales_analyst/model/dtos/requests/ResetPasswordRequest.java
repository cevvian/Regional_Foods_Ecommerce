package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank(message = "Mật khẩu cũ không được để trống")
    private String password;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải có ít nhất 8 ký tự.")
    private String newPassword;

    @NotBlank(message = "Mật khẩu nhập lại không được để trống")
    private String confirmPassword;
}
