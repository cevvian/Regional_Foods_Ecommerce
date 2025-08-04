package edu.ut.sales.sales_analyst.model.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
    @JsonProperty("refresh_token")
    @NotBlank(message = "Refresh token is required") String refreshToken;
}
