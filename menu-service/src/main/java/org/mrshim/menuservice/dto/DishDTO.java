package org.mrshim.menuservice.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO для создания/изменения блюда
 */
@Data
public class DishDTO {

    /**
     * Название блюда
     */
    private String name;
    /**
     * Список ингредиентов
     */
    private List<String> ingredients;
    /**
     * Стоимость
     */
    private BigDecimal price;
    /**
     * Описание блюда
     */
    private String description;
}
