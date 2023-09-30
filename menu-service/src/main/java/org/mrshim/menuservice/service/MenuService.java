package org.mrshim.menuservice.service;

import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.dto.RequestListStockDto;
import org.mrshim.menuservice.exception.DishNotFoundException;
import org.mrshim.menuservice.model.Dish;

import java.util.List;


public interface MenuService {
    /**
     * Метод для создания блюда
     *
     * @param createDishRequest Параметры блюда
     */
    void createDish(CreateDishRequestDto createDishRequest);

    /**
     * Метод для проверки названий и стоимости блюд с бд
     *
     * @param requestListStockDto список блюд
     * @return boolean. tru если значения запроса со списоком блюд совпадает с данными в бд. Иначе false
     */
    boolean isInStock(RequestListStockDto requestListStockDto);

    /**
     * Метод для получения списка блюд ресторана
     *
     * @return List<Dish> список блюд
     */
    List<Dish> getAllDishes();


    /**
     * Метод для получения блюда по его индефикатору
     *
     * @param id индфикатор блюдо
     * @return Dish найденное блюдо
     * @throws DishNotFoundException если блюдо с указанным индефикатором не найдено
     */
    Dish getDish(String id);

    void deleteDish(String id);

    /**
     * Метод для изменения блюда ресторана
     *
     * @param id индефикатор блюда
     * @param createDishRequestDto Параметры блюда
     * @return Dish измененное блюдо
     * @throws DishNotFoundException если блюдо с указанным индефикатором не найдено
     */
    Dish editDish(String id, CreateDishRequestDto createDishRequestDto);
}
