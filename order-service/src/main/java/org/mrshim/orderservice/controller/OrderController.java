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
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest createOrderRequest )  {


        Long orderId = orderService.createOrder(createOrderRequest);

        return new ResponseEntity<>(orderId,HttpStatus.OK);


    }

    @GetMapping
    public ResponseEntity<?> getAllOrders()
    {
        List<Order> allOrders = orderService.getAllOrders();

        return new ResponseEntity<>(allOrders,HttpStatus.OK);

    }

    @GetMapping("/history")
    public ResponseEntity<?> getUserOderHistory()
    {

        List<Order> userOderHistory = orderService.getUserOderHistory();

        return new ResponseEntity<>(userOderHistory,HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id)
    {
        Order orderById = orderService.getOrderById(id);


        return new ResponseEntity<>(orderById,HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id)
    {

        orderService.cancelOrder(id);

        return new ResponseEntity<>(HttpStatus.OK);

    }



}
