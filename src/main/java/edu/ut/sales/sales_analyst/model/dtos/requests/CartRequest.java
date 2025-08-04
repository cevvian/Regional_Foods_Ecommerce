package edu.ut.sales.sales_analyst.model.dtos.requests;

import edu.ut.sales.sales_analyst.model.entities.CartItem;
import edu.ut.sales.sales_analyst.model.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
//    private String cartId;
    private String userId;
}
