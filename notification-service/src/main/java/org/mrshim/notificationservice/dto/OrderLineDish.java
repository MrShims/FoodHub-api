package org.mrshim.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderLineDish{
    private Long id;

    private String name;

    private int quantity;

    private BigDecimal price;


}
