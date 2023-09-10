package org.mrshim.menuservice.controller;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.service.MenuService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<?> createDish(@RequestBody CreateDishRequestDto createDishRequest)
    {
        menuService.createDish(createDishRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> getAllDishes()
    {
        List<Dish> allDishes = menuService.getAllDishes();

        return new ResponseEntity<>(allDishes,HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDish(@PathVariable String id)
    {
        Optional<Dish> dish = menuService.getDish(id);

        if (dish.isPresent()) return new ResponseEntity<>(dish.get(),HttpStatus.OK);

        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDish(@PathVariable String id)
    {
        boolean b = menuService.deleteDish(id);

        if (b) return new ResponseEntity<>(HttpStatus.OK);

        else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }





}
