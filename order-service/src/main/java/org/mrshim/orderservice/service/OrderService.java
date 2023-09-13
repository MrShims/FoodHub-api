package org.mrshim.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.mrshim.orderservice.dto.CreateOrderRequest;
import org.mrshim.orderservice.dto.OrderLineDishesRequest;
import org.mrshim.orderservice.exception.OrderNotFoundException;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.model.OrderLineDish;
import org.mrshim.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    public Long createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order();

        order.setContact(createOrderRequest.getContact());
        order.setOrderAmount(createOrderRequest.getOrderAmount());
        order.setStatus("Принят");
        order.setCreationDate(LocalDateTime.now());
        order.setUpdateDate(LocalDateTime.now());
        order.setDeliveryAddress(createOrderRequest.getDeliveryAddress());
        order.setLineDishes(mapToOrderList(createOrderRequest.getLineDishes()));
        order.setUserId(createOrderRequest.getUserId());
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());


        return orderRepository.save(order).getId();
    }

    private List<OrderLineDish> mapToOrderList(List<OrderLineDishesRequest> orderLineDishesRequests) {
        ArrayList<OrderLineDish> orderLineDishArrayList = new ArrayList<>();

        for (int i = 0; i < orderLineDishesRequests.size(); i++) {

            OrderLineDishesRequest orderLineDishesRequest = orderLineDishesRequests.get(i);

            OrderLineDish orderLineDish = new OrderLineDish();
            orderLineDish.setQuantity(orderLineDishesRequest.getQuantity());
            orderLineDish.setName(orderLineDishesRequest.getName());

            orderLineDishArrayList.add(orderLineDish);


        }

        return orderLineDishArrayList;


    }

    public List<Order> getAllOrders() {

        return orderRepository.findAll();


    }

    public Order getOrderById(Long id) {

        Optional<Order> byId = orderRepository.findById(id);

        if (byId.isPresent()) {
            return byId.get();
        } else throw new OrderNotFoundException("Заказ не найден");


    }

    public void cancelOrder(Long id) {
        Optional<Order> byId = orderRepository.findById(id);

        if (byId.isPresent()) {

            Order order = byId.get();

            order.setStatus("Отменен");

            orderRepository.save(order);


        } else throw new OrderNotFoundException("Заказ с id" + id + " не найден");


    }


}
