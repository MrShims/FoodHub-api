package org.mrshim.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPlacedEvent {

    private String email;

    private Long id;

    private List<OrderLineDish> LineDishes;

    private String deliveryAddress;

    private BigDecimal orderAmount;
}
