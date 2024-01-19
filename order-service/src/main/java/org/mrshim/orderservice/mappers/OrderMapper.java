package org.mrshim.orderservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mrshim.orderservice.dto.CreateOrderRequest;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.model.OrderStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "lineDishes", source = "createOrderRequest.lineDishes")
    @Mapping(target = "contact", source = "createOrderRequest.contact")
    @Mapping(target = "orderStatus", source = "orderStatus")
    @Mapping(target = "creationDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updateDate", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "userId", expression = "java(authentication.getToken().getClaim(\"sub\").toString())")
    @Mapping(target = "orderAmount", source = "sumOrder")
    @Mapping(target = "deliveryAddress", source = "createOrderRequest.deliveryAddress")
    @Mapping(target = "paymentMethod", source = "createOrderRequest.paymentMethod")
    Order mapToOrder(CreateOrderRequest createOrderRequest, BigDecimal sumOrder, JwtAuthenticationToken authentication, OrderStatus orderStatus);
}
