package edu.ut.sales.sales_analyst.model.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionRequest {
    @NotBlank(message = "Region name is required")
    private String regionName;
}
