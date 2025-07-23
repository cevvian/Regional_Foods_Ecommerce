package edu.ut.sales.sales_analyst.model.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {
    private String userId;
    private String userName;
    private String email;
    private String phone;
    private String password;
    private Boolean isActive;
    private AddressResponse address;
    private Date createAt;
}
