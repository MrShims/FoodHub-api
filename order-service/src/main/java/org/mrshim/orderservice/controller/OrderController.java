package org.mrshim.orderservice.controller;


import lombok.RequiredArgsConstructor;
import org.mrshim.orderservice.dto.CreateOrderRequest;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController
{
    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest)
    {

        Long order = orderService.createOrder(createOrderRequest);

        return new ResponseEntity<>(order, HttpStatus.CREATED);

    }

    @GetMapping
    public ResponseEntity<?> getAllOrders()
    {
        List<Order> allOrders = orderService.getAllOrders();

        return new ResponseEntity<>(allOrders,HttpStatus.OK);

    }
}
