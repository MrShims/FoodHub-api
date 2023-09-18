package org.mrshim.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mrshim.orderservice.model.OrderLineDish;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPlacedEvent {

    private Long id;
    private LocalDateTime creationDate;
    private List<OrderLineDish> LineDishes;
    private String deliveryAddress;
    private BigDecimal orderAmount;

    private String userEmail;

    private String userName;



}
