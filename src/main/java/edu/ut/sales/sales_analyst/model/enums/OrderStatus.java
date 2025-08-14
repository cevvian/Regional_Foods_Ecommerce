package edu.ut.sales.sales_analyst.model.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatus {
    PENDING("Your order is pending confirmation"),
    CONFIRM("Your order has been confirmed"),
    SHIPPED("Your order has been shipped"),
    COMPLETED("Your order has been completed"),
    CANCELLED("Your order has been cancelled");

    private final String message;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case PENDING -> (newStatus == CONFIRM || newStatus == CANCELLED);
            case CONFIRM -> (newStatus == SHIPPED);
            case SHIPPED -> (newStatus == COMPLETED);
            default -> false; // FAILED, REFUNDED không đổi trạng thái
        };
    }

}
