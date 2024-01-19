package org.mrshim.transactionalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private List<OrderResponseListDishes> lineDishes;
    private String deliveryAddress;
    private BigDecimal orderAmount;
    private String status;
}
