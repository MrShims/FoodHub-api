package org.mrshim.orderservice.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.mrshim.orderservice.model.OrderLineDish;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private Long userId;

    private String status;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private List<OrderLineDishesRequest> LineDishes;

    private String deliveryAddress;

    private BigDecimal orderAmount;

    private String paymentMethod;

    private String contact;



}
