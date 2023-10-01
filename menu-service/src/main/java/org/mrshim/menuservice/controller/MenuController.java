package org.mrshim.menuservice.controller;


import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.dto.RequestListStockDto;
import org.mrshim.menuservice.dto.RequestStockDto;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.service.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuService menuService;


    @Operation(
            description = "Post endpoint for crate dish",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CreateDishRequestDto.class))
                    },
                    required = true
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    ),
            }
    )
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createDish(@RequestBody CreateDishRequestDto createDishRequest) {


        menuService.createDish(createDishRequest);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @PostMapping("/stock")
    public boolean isInStock(@RequestBody(required = true) RequestListStockDto RequestListStockDto) {


        boolean inStock = menuService.isInStock(RequestListStockDto);

        return inStock;

    }

    @Operation(
            description = "Get endpoint for dishes list",
            responses = {
                    @ApiResponse(
                            content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = Dish.class))),
                            responseCode = "200",
                            description = "OK"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    ),
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllDishes() {

        log.info("Checking get method");
        List<Dish> allDishes = menuService.getAllDishes();

        return new ResponseEntity<>(allDishes, HttpStatus.OK);

    }






    @GetMapping("/{id}")
    @Observed(name = "Controller Method getDish")
    public ResponseEntity<?> getDish(@PathVariable String id) {
        Dish dish = menuService.getDish(id);

        return new ResponseEntity<>(dish, HttpStatus.OK);


    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteDish(@PathVariable(required = true) String id) {

        menuService.deleteDish(id);

        return new ResponseEntity<>(HttpStatus.OK);


    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> editDish(@PathVariable(required = true) String id, @RequestBody(required = true) CreateDishRequestDto createDishRequestDto) {
        Dish dish = menuService.editDish(id, createDishRequestDto);


        return new ResponseEntity<>(dish, HttpStatus.OK);


    }


}
