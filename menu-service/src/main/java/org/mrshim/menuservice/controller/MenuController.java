package org.mrshim.menuservice.controller;


import lombok.RequiredArgsConstructor;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody CreateDishRequestDto createDishRequest) {
        menuService.createDish(createDishRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> getAllDishes() {
        List<Dish> allDishes = menuService.getAllDishes();

        return new ResponseEntity<>(allDishes, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDish(@PathVariable String id) {
        Dish dish = menuService.getDish(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable(required = true) String id) {

        menuService.deleteDish(id);

        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editDish(@PathVariable(required = true) String id, @RequestBody(required = true) CreateDishRequestDto createDishRequestDto) {
        Dish dish = menuService.editDish(id, createDishRequestDto);


        return new ResponseEntity<>(dish, HttpStatus.OK);


    }


}
