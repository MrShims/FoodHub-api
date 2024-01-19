package org.mrshim.menuservice.controller;


import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mrshim.menuservice.api.PageableResponse;
import org.mrshim.menuservice.dto.DishDTO;
import org.mrshim.menuservice.dto.RequestListStockDto;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.service.MenuService;
import org.mrshim.menuservice.utils.Authorities;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Menu controller")
@SecurityRequirement(name = "bearerAuth")
public class MenuController {

    private final MenuService menuService;

    @Operation(description = "Post endpoint for crate dish")
    @ApiResponse(responseCode = "201", description = "Created")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @PostMapping
    @Secured(Authorities.ADMIN)
    public ResponseEntity<?> createDish(@RequestBody DishDTO createDishRequest) {
        menuService.createDish(createDishRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/stock")
    public ResponseEntity<?> isInStock(@RequestBody RequestListStockDto RequestListStockDto) {
        boolean inStock = menuService.isInStock(RequestListStockDto);
        return new ResponseEntity<>(inStock, HttpStatus.OK);
    }

    @Operation(description = "Get endpoint for dishes list")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @GetMapping
    public ResponseEntity<?> getAllDishes(@RequestParam(value = "page_number", required = false, defaultValue = "0") Integer pageNumber,
                                          @RequestParam(value = "limit", required = false, defaultValue = "10") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        PageableResponse<DishDTO> allDishes = menuService.getAllDishes(pageable);
        return new ResponseEntity<>(allDishes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Observed(name = "Controller Method getDish")
    public ResponseEntity<?> getDish(@PathVariable String id) {
        Dish dish = menuService.getDish(id);
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @Secured(Authorities.ADMIN)
    public ResponseEntity<?> deleteDish(@PathVariable String id) {
        menuService.deleteDish(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("{id}")
    @Secured(Authorities.ADMIN)
    public ResponseEntity<?> editDish(@PathVariable(required = true) String id, @RequestBody(required = true) DishDTO dishDTO) {
        Dish dish = menuService.editDish(id, dishDTO);
        return new ResponseEntity<>(dish, HttpStatus.OK);
    }
}
