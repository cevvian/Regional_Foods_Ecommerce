package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequest {

    @NotBlank(message = "User's name is required")
    private String userName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{10,11}$", message = "Invalid phone number")
    private String phone;

    private Date createAt;
}
