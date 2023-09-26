package org.mrshim.transactionalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentEvent {

    private String email;

    private Long id;

    private List<OrderResponseListDishes> LineDishes;

    private String deliveryAddress;

    private BigDecimal orderAmount;



}
