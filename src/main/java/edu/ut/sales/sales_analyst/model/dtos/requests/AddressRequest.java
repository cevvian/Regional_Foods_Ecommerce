package edu.ut.sales.sales_analyst.model.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String userId;
    private String addressLine;
    private String province;
    private String phone;
    private Boolean isDefault;
}
