package org.mrshim.transactionalservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mrshim.transactionalservice.dto.OrderResponse;
import org.mrshim.transactionalservice.dto.OrderResponseListDishes;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface TransactionalMapper {

    @Mapping(source = "orderResponse.id", target = "id")
    @Mapping(source = "orderResponse.deliveryAddress", target = "deliveryAddress")
    @Mapping(source = "orderResponse.orderAmount", target = "orderAmount", qualifiedByName = "bigDecimalToString")
    @Mapping(source = "email", target = "email")
    @Mapping(target = "lineDishes", expression = "java(mapDishesList(orderResponse.getLineDishes()))")
    org.mrshim.foodhub.PaymentNotificationMessage mapOrderResponseToPaymentNotificationMessage(OrderResponse orderResponse, String email);

    @Named("bigDecimalToString")
    default String bigDecimalToString(BigDecimal value) {
        return value == null ? "0.0" : value.toPlainString();
    }

    default List<org.mrshim.foodhub.OrderListMessage> mapDishesList(List<OrderResponseListDishes> dishesList) {
        return dishesList.stream()
                .map(this::mapDish)
                .collect(Collectors.toList());
    }

    @Mapping(source = "price", target = "price", qualifiedByName = "bigDecimalToString")
    org.mrshim.foodhub.OrderListMessage mapDish(OrderResponseListDishes dish);
}

