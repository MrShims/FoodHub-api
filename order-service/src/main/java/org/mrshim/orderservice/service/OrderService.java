package org.mrshim.orderservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.mrshim.orderservice.dto.*;
import org.mrshim.orderservice.enums.OrderStatusEnum;
import org.mrshim.orderservice.exception.CreateOrderException;
import org.mrshim.orderservice.exception.OrderNotFoundException;
import org.mrshim.orderservice.mappers.OrderMapper;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.model.OrderLineDish;
import org.mrshim.orderservice.model.OrderStatus;
import org.mrshim.orderservice.repository.OrderRepository;
import org.mrshim.orderservice.repository.OrderStatusRepository;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClient;

    private final LoadBalancerClient loadBalancerClient;
    private final OrderMapper orderMapper;

    private final DiscoveryClient discoveryClient;
    private final OrderStatusRepository orderStatusRepository;

    private boolean isInStockDishes(List<OrderLineDishesRequest> lineDishes) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("menu-service");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lineDishes", lineDishes);

        String jsonString = jsonObject.toString();
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String token = authentication.getToken().getTokenValue();

        String serviceUrl = "http://menu-service";

        Boolean block = webClient.baseUrl(serviceUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .build().post().uri("/menu/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();


        return block;

    }


    private BigDecimal calculateSumOrder(List<OrderLineDishesRequest> lineDishes) {
        double a = 0;

        for (int i = 0; i < lineDishes.size(); i++) {

            OrderLineDishesRequest orderLineDishesRequest = lineDishes.get(i);

            a += orderLineDishesRequest.getPrice().doubleValue() * orderLineDishesRequest.getQuantity();

        }
        return new BigDecimal(a);
    }

    public Long createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order();

        if (!isInStockDishes(createOrderRequest.getLineDishes()))
            throw new CreateOrderException("Ошибка формирования заказа");

        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Object userId = authentication.getToken().getClaim("sub");
        List<OrderLineDishesRequest> lineDishes = createOrderRequest.getLineDishes();
        BigDecimal sumOrder = calculateSumOrder(lineDishes);
        Optional<OrderStatus> byStatusName = orderStatusRepository.findByStatusName(OrderStatusEnum.ACCEPT);
        Order order1 = orderMapper.mapToOrder(createOrderRequest, sumOrder, authentication,byStatusName.get());


/*        order.setContact(createOrderRequest.getContact());
        order.setStatus("Принят");
        order.setCreationDate(LocalDateTime.now());
        order.setUpdateDate(LocalDateTime.now());
        order.setUserId(userId.toString());
        order.setOrderAmount(sumOrder);
        order.setDeliveryAddress(createOrderRequest.getDeliveryAddress());
        order.setLineDishes(mapToOrderList(createOrderRequest.getLineDishes()));
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());*/
        Order save = orderRepository.save(order1);
        return save.getId();
    }
/*    private List<OrderLineDish> mapToOrderList(List<OrderLineDishesRequest> orderLineDishesRequests) {
        ArrayList<OrderLineDish> orderLineDishArrayList = new ArrayList<>();
        for (int i = 0; i < orderLineDishesRequests.size(); i++) {
            OrderLineDishesRequest orderLineDishesRequest = orderLineDishesRequests.get(i);
            OrderLineDish orderLineDish = new OrderLineDish();
            orderLineDish.setQuantity(orderLineDishesRequest.getQuantity());
            orderLineDish.setName(orderLineDishesRequest.getName());
            orderLineDish.setPrice(orderLineDishesRequest.getPrice());
            orderLineDishArrayList.add(orderLineDish);
        }
        return orderLineDishArrayList;
    }*/

    public List<Order> getUserOderHistory() {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getToken().getClaim("sub");
        List<Order> allOrdersByUserId = orderRepository.findAllByUserId(userId);
        return allOrdersByUserId;
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

            //order.setStatus("Отменен");

            orderRepository.save(order);


        } else throw new OrderNotFoundException("Заказ с id" + id + " не найден");


    }

/*    public void editStatusToOrder(EditStatusDto editStatusDto, Long id) {
        String status = editStatusDto.getStatus();
        Optional<Order> byId = orderRepository.findById(id);
        if (byId.isPresent()) {
            Order order = byId.get();
            order.setStatus(status);
            orderRepository.save(order);
            if (editStatusDto.getStatus().equals("Готовится")) {
                RabbitMessageDto rabbitMessageDto = new RabbitMessageDto();
                rabbitMessageDto.setId(order.getId());
                rabbitMessageDto.setDeliveryAddress(order.getDeliveryAddress());
                try {
                    String s = objectMapper.writeValueAsString(rabbitMessageDto);
                    rabbitTemplate.convertAndSend(exchange, routingKey, s);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }


        } else throw new OrderNotFoundException("Заказ не найден");

    }*/


    public List<AvailableOrderResponseDTO> getAvailableOrderList() {

        return null;
       // List<Order> AvailableOrderList = orderRepository.findByStatus("Оплачен");
        //return AvailableOrderList.stream().map(this::mapToAvailableResponseOrder).collect(Collectors.toList());
    }

    public AvailableOrderResponseDTO mapToAvailableResponseOrder(Order order) {

        AvailableOrderResponseDTO availableOrderResponseDTO = new AvailableOrderResponseDTO();
        availableOrderResponseDTO.setId(order.getId());
        availableOrderResponseDTO.setUpdateDate(order.getUpdateDate());
        List<AvailableOrderResponseListDTO> list = new ArrayList<>();
        List<OrderLineDish> lineDishes = order.getLineDishes();
        for (OrderLineDish lineDish : lineDishes) {
            AvailableOrderResponseListDTO availableOrderResponseListDTO = new AvailableOrderResponseListDTO();
            availableOrderResponseListDTO.setName(lineDish.getName());
            availableOrderResponseListDTO.setQuantity(lineDish.getQuantity());
            list.add(availableOrderResponseListDTO);
        }
        availableOrderResponseDTO.setLineDishes(list);
        return availableOrderResponseDTO;
    }


}
