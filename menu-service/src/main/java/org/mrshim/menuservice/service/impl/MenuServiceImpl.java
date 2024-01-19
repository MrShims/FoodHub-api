package org.mrshim.menuservice.service.impl;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshim.menuservice.api.PageableResponse;
import org.mrshim.menuservice.dto.DishDTO;
import org.mrshim.menuservice.dto.RequestListStockDto;
import org.mrshim.menuservice.dto.RequestStockDto;
import org.mrshim.menuservice.exception.DishNotFoundException;
import org.mrshim.menuservice.mappers.MenuMapper;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.repository.MenuRepository;
import org.mrshim.menuservice.service.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    /**
     * Метод для создания блюда
     *
     * @param createDishRequest Параметры блюда
     */
    @Override
    public void createDish(DishDTO createDishRequest) {
        Dish dish = menuMapper.fromRequestDTO(createDishRequest);
        Dish save = menuRepository.save(dish);
        log.info("Created dish with id - {}", save.getId());
    }

    /**
     * Метод для проверки названий и стоимости блюд с бд
     *
     * @param requestListStockDto список блюд
     * @return boolean. tru если значения запроса со списоком блюд совпадает с данными в бд. Иначе false
     */
    @Override
    public boolean isInStock(RequestListStockDto requestListStockDto) {
        List<Dish> all = menuRepository.findAll();
        Map<String, Dish> dishMap = all.stream().collect(Collectors.toMap(Dish::getName, dish -> dish));
        List<RequestStockDto> lineDishes = requestListStockDto.getLineDishes();

        for (RequestStockDto requestStockDto : lineDishes) {
            Dish dish = dishMap.get(requestStockDto.getName());
            if (dish == null || requestStockDto.getPrice().doubleValue() / requestStockDto.getQuantity() != dish.getPrice().doubleValue()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Метод для получения списка блюд ресторана
     *
     * @return PageableResponse<Dish> Pageable список блюд
     */
    @Override
    public PageableResponse<DishDTO> getAllDishes(Pageable pageable) {
        Page<DishDTO> all = menuRepository.findAll(pageable).map(menuMapper::toDTO);
        return new PageableResponse<DishDTO>(all.getContent(), all.getTotalPages(), all.getTotalElements());
    }

    /**
     * Метод для получения блюда по его индефикатору
     *
     * @param id индфикатор блюдо
     * @return Dish найденное блюдо
     * @throws DishNotFoundException если блюдо с указанным индефикатором не найдено
     */
    @Override
    @Observed(name = "Method getDish")
    public Dish getDish(String id) {
        Optional<Dish> dishById = menuRepository.findById(id);
        if (dishById.isPresent()) {
            return dishById.get();
        }
        log.info("Dish with id - " + id + " not found");
        throw new DishNotFoundException("Dish with id - " + id + " not found");
    }

    /**
     * Метод для удаления блюда
     *
     * @param id индефикатор блюда
     * @throws DishNotFoundException если блюдо с указанным индефикатором не найдено
     */
    @Override
    public void deleteDish(String id) {
        Optional<Dish> dishById = menuRepository.findById(id);
        if (dishById.isPresent()) {
            menuRepository.delete(dishById.get());
            log.info("Dish with id [{}] was deleted", id);
            return;
        }
        log.info("Dish with id - {} not found", id);
        throw new DishNotFoundException("Dish with id - " + id + " not found");
    }

    /**
     * Метод для изменения блюда ресторана
     *
     * @param id      индефикатор блюда
     * @param dishDTO Параметры блюда
     * @return Dish измененное блюдо
     * @throws DishNotFoundException если блюдо с указанным индефикатором не найдено
     */
    @Override
    public Dish editDish(String id, DishDTO dishDTO) {
        Optional<Dish> dishById = menuRepository.findById(id);
        if (dishById.isPresent()) {
            Dish dish = dishById.get();
            dish.setName(dishDTO.getName());
            dish.setPrice(dishDTO.getPrice());
            dish.setDescription(dishDTO.getDescription());
            dish.setIngredients(dishDTO.getIngredients());
            return menuRepository.save(dish);
        }
        log.info("Dish with id - " + id + " not found");
        throw new DishNotFoundException("Dish with id - " + id + " not found");
    }
}
