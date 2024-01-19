package org.mrshim.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrshim.orderservice.enums.PaymentTypeEnum;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotNull
    private List<OrderLineDishesRequest> lineDishes;
    @NotBlank
    private String deliveryAddress;
    private PaymentTypeEnum paymentMethod;
    @NotBlank
    private String contact;
}
