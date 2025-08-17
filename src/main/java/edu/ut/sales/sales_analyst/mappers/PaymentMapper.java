package edu.ut.sales.sales_analyst.mappers;

import edu.ut.sales.sales_analyst.model.dtos.responses.PaymentResponse;
import edu.ut.sales.sales_analyst.model.entities.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper {
    @Mapping(target = "orderId", source = "order.orderId")
    PaymentResponse.PaymentInfoResponse toPaymentInfoResponse(Payment payment);
}
