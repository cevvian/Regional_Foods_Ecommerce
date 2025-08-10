package edu.ut.sales.sales_analyst.model.enums;


public enum OrderStatus {
    PENDING,
    CONFIRM,
    SHIPPED,
    COMPLETED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus newStatus) {
        return switch (this) {
            case PENDING -> (newStatus == CONFIRM || newStatus == CANCELLED);
            case CONFIRM -> (newStatus == SHIPPED);
            case SHIPPED -> (newStatus == COMPLETED);
            default -> false; // FAILED, REFUNDED không đổi trạng thái
        };
    }

}
