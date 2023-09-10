package org.mrshim.menuservice.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateDishRequestDto {


    private String name;
    private List<String> ingredients;
    private BigDecimal price;
    private String description;


}
