package org.mrshim.transactionalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseListDishes {
    private Long id;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}
