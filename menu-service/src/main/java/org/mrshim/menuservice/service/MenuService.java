package org.mrshim.menuservice.service;

import org.mrshim.menuservice.api.PageableResponse;
import org.mrshim.menuservice.dto.DishDTO;
import org.mrshim.menuservice.dto.RequestListStockDto;
import org.mrshim.menuservice.exception.DishNotFoundException;
import org.mrshim.menuservice.model.Dish;
import org.springframework.data.domain.Pageable;


public interface MenuService {
    void createDish(DishDTO createDishRequest);
    boolean isInStock(RequestListStockDto requestListStockDto);
    PageableResponse<DishDTO> getAllDishes(Pageable pageable);
    Dish getDish(String id);

    void deleteDish(String id);

    Dish editDish(String id, DishDTO dishDTO);
}
