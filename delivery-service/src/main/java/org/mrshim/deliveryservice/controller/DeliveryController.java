package org.mrshim.deliveryservice.controller;

import lombok.RequiredArgsConstructor;
import org.mrshim.deliveryservice.dto.AvailableOrdersResponseDto;
import org.mrshim.deliveryservice.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery")
@RequiredArgsConstructor
public class DeliveryController {


    private final DeliveryService deliveryService;


    @GetMapping
    public ResponseEntity<?> getAvailableOrders()
    {

        List<AvailableOrdersResponseDto> availableOrders = deliveryService.getAvailableOrders();

        return new ResponseEntity<>(availableOrders, HttpStatus.OK);


    }



    @PostMapping("/accept/{OrderId}")
    public ResponseEntity<?> acceptOrder(@PathVariable Long OrderId)
    {
        deliveryService.AcceptOrder(OrderId);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);





    }

    @GetMapping("/history")
    public ResponseEntity<?> getOrderDeliveryHistory()
    {

        List<AvailableOrdersResponseDto> history = deliveryService.getHistory();

        return new ResponseEntity<>(history,HttpStatus.OK);


    }

    @PostMapping("/delivery/{OrderId}")

    public ResponseEntity<?> deliveryOrder(@PathVariable Long OrderId)
    {
        deliveryService.deliveryOrder(OrderId);

        return new ResponseEntity<>(HttpStatus.OK);


    }





}
