package org.mrshim.menuservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO для списка ингредиентов блюда
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestListStockDto {

    /**
     * Список ингредиетов
     */
    private List<RequestStockDto> lineDishes;


}
