package edu.ut.sales.sales_analyst.model.enums;

public enum PaymentStatus {
    PROCESSING,
    FAILED,
    PAID,
    REFUNDED;

    public boolean canTransitionTo(PaymentStatus newStatus) {
        return switch (this) {
            case PROCESSING -> (newStatus == PAID || newStatus == FAILED);
            case PAID -> (newStatus == REFUNDED);
            default -> false; // FAILED, REFUNDED không đổi trạng thái
        };
    }
}
