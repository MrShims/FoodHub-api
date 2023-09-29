package org.mrshim.menuservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mrshim.menuservice.dto.CreateDishRequestDto;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class MenuControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    private MenuRepository menuRepository;

    @Container
    private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");


    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }


    private CreateDishRequestDto getDishRequest() {


        CreateDishRequestDto createDishRequestDto = new CreateDishRequestDto();
        createDishRequestDto.setName("Pizza");
        createDishRequestDto.setPrice(new BigDecimal(650));
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("cheese");
        ingredients.add("Tomato");
        createDishRequestDto.setIngredients(ingredients);
        createDishRequestDto.setDescription("Pizza with");

        return createDishRequestDto;


    }


    @Test
    void should_createDishTest() throws Exception {


        CreateDishRequestDto dishRequest = getDishRequest();

        String s = objectMapper.writeValueAsString(dishRequest);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)
        ).andExpect(status().isCreated());


    }

    public Dish createDish() {


        Dish dish = Dish.builder()
                .name("Burger")
                .price(new BigDecimal(650))
                .description("Best Burger")
                .ingredients(List.of("Tomato", "Cheese"))
                .build();

        return dish;


    }


    @Test
    void getAllDishesTest() throws Exception {

        menuRepository.save(createDish());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)

                );


    }

    @Test
    void getDishByIdTest() throws Exception {

        Dish save = menuRepository.save(createDish());


        String id = save.getId();


        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu/" + id))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                                               
                                {
                                  "name": "Burger",
                                  "price": 650,
                                  "ingredients": ["Tomato", "Cheese"],
                                  "description": "Best Burger"
                                }
                                                               
                                """)
                );


    }


    @Test
    void ShouldThrowDishNotFoundException() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu/trewtwt")).andExpectAll(
        ).andExpect(status().isNotFound());
    }


    public CreateDishRequestDto createEditDishDto() {
        CreateDishRequestDto createDishRequestDto = new CreateDishRequestDto();
        createDishRequestDto.setName("Salad");
        createDishRequestDto.setPrice(new BigDecimal(555));
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("Lettuce");
        ingredients.add("Cucumber");
        createDishRequestDto.setIngredients(ingredients);
        createDishRequestDto.setDescription("Fresh and healthy salad with a mix of vegetables dressed with olive oil.");


        return createDishRequestDto;


    }


    @Test
    void shouldEditDishTest() throws Exception {

        Dish save = menuRepository.save(createDish());
        CreateDishRequestDto editDishDto = createEditDishDto();

        String s = objectMapper.writeValueAsString(editDishDto);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/menu/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                                                               
                                {
                                  "name": "Salad",
                                  "price": 555,
                                  "ingredients": ["Lettuce", "Cucumber"],
                                  "description": "Fresh and healthy salad with a mix of vegetables dressed with olive oil."
                                }
                                                               
                                """)
                );


    }


}