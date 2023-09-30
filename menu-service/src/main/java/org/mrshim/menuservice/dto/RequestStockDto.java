package org.mrshim.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO для проверки приходящего списка блюд с данными в бд
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestStockDto {


    /**
     * Название блюда
     */
    private String name;

    /**
     * Количество
     */
    private int quantity;

    /**
     * Цена
     */
    private BigDecimal price;



}
