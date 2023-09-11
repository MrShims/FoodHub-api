package org.mrshim.menuservice.service;

import lombok.RequiredArgsConstructor;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.exception.DishNotFoundException;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;


    public void createDish(CreateDishRequestDto createDishRequest) {

        Dish newDish = Dish.builder()
                .name(createDishRequest.getName())
                .ingredients(createDishRequest.getIngredients())
                .price(createDishRequest.getPrice())
                .description(createDishRequest.getDescription())
                .build();


        menuRepository.save(newDish);


    }

    public List<Dish> getAllDishes()
    {

        return menuRepository.findAll();

    }

    public Dish getDish(String id)
    {

        Optional<Dish> byId = menuRepository.findById(id);

        if (byId.isPresent()) return byId.get();

        else throw new DishNotFoundException("Блюдо с id - "+id+" не найдено");


    }

    public void deleteDish(String id)
    {
        Optional<Dish> byId = menuRepository.findById(id);

        if (byId.isPresent())
        {
            menuRepository.delete(byId.get());

        }

        else throw new DishNotFoundException("Блюдо с id - "+id+" не найдено");



    }


    public Dish editDish(String id, CreateDishRequestDto createDishRequestDto)
    {
        Optional<Dish> byId = menuRepository.findById(id);


        if (byId.isPresent())
        {

            Dish dish = byId.get();

            dish.setName(createDishRequestDto.getName());
            dish.setPrice(createDishRequestDto.getPrice());
            dish.setDescription(createDishRequestDto.getDescription());
            dish.setIngredients(createDishRequestDto.getIngredients());

            return menuRepository.save(dish);


        }

        else throw new DishNotFoundException("Блюдо с id - "+id+" не найдено");


    }


}
