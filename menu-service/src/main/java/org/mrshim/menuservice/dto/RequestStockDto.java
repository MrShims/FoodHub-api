package org.mrshim.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStockDto {


    private String name;

    private int quantity;

    private BigDecimal price;



}
