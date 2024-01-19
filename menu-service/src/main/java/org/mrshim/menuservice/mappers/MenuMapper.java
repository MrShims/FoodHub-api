package org.mrshim.menuservice.mappers;

import org.mapstruct.Mapper;
import org.mrshim.menuservice.dto.DishDTO;
import org.mrshim.menuservice.model.Dish;

@Mapper(componentModel = "spring")
public interface MenuMapper {

    Dish fromRequestDTO(DishDTO dto);

    DishDTO toDTO(Dish dish);
}
