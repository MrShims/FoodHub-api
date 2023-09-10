package org.mrshim.menuservice.service;

import lombok.RequiredArgsConstructor;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
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

    public Optional<Dish> getDish(String id)
    {

        return menuRepository.findById(id);


    }

    public boolean deleteDish(String id)
    {
        Optional<Dish> byId = menuRepository.findById(id);

        if (byId.isPresent())
        {
            menuRepository.delete(byId.get());
            return true;
        }

        return false;



    }


}
