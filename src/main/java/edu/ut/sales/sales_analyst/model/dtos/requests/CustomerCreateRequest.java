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
public class CustomerCreateRequest {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{10,11}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Address cannot be blank")
    private String address;
    private Date createAt;
}
