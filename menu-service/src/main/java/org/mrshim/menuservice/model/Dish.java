package org.mrshim.menuservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

/**
 * Модель для блюда
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "dish")
public class Dish {

    /**
     * Индефикатор блюда
     */
    @Id
    private String id;
    /**
     * Название блюда
     */
    private String name;
    /**
     * Список ингредиентов
     */
    private List<String> ingredients;
    /**
     * Стоимость блюда
     */
    private BigDecimal price;
    /**
     * Описание блюда
     */
    private String description;
}
