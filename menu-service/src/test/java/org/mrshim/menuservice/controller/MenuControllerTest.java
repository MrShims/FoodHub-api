package org.mrshim.menuservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.MongoDBContainerConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mrshim.menuservice.model.Dish;
import org.mrshim.menuservice.repository.MenuRepository;
import org.mrshim.menuservice.utils.Authorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;
import testentity.DishTestBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = MongoDBContainerConfig.Initializer.class)
class MenuControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN})
    @DisplayName("Проверка создания блюда")
    void Test1() throws Exception {
        Dish build = DishTestBuilder.aDish().build();
        String s = objectMapper.writeValueAsString(build);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("Проверка получения списка блюд")
    void Test2() throws Exception {
        menuRepository.save(DishTestBuilder.aDish().build());
        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu"))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @WithMockUser
    @DisplayName("Проверка получения блюда по id")
    void Test3() throws Exception {
        Dish save = menuRepository.save(DishTestBuilder.aDish()
                .withName("Burger")
                .withPrice(new BigDecimal(650))
                .withIngredients(List.of("Tomato", "Cheese"))
                .withDescription("Best Burger").build());
        String id = save.getId();

        String jsonContent = readJsonFromFile(ResourceUtils.getFile("classpath:dish.json").getPath());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu/" + id))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(jsonContent)
                );
    }

    @Test
    @WithMockUser
    @DisplayName("Должна вылететь ошибка при поиске блюда")
    void Test5() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/menu/trewtwt")).andExpectAll(
        ).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = {Authorities.ADMIN})
    @DisplayName("Проверка обновления блюда")
    void Test4() throws Exception {
        Dish build = DishTestBuilder.aDish().build();
        Dish save = menuRepository.save(build);
        String jsonContent = readJsonFromFile(ResourceUtils.getFile("classpath:dish.json").getPath());
        this.mockMvc.perform(MockMvcRequestBuilders.put("/menu/" + save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpectAll(
                        status().isOk(),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json(jsonContent)
                );
    }

    @Test
    @WithMockUser(authorities = {"ROLE_fadsfsdafasf"})
    @DisplayName("Проверка method security")
    void Test6() throws Exception {
        Dish build = DishTestBuilder.aDish().build();
        String s = objectMapper.writeValueAsString(build);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)
        ).andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Проверка фильтров")
    void Test7() throws Exception {
        Dish build = DishTestBuilder.aDish().build();
        String s = objectMapper.writeValueAsString(build);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(s)
        ).andExpect(status().isUnauthorized());
    }

    public static String readJsonFromFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}