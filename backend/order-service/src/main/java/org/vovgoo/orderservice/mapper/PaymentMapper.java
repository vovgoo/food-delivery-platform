package org.vovgoo.orderservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vovgoo.orderservice.dto.payment.response.PaymentResponse;
import org.vovgoo.orderservice.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "method", target = "method")
    @Mapping(source = "status", target = "status")
    PaymentResponse toResponse(Payment payment);
}
