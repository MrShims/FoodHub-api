package org.mrshim.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.json.JSONObject;
import org.mrshim.orderservice.dto.CreateOrderRequest;
import org.mrshim.orderservice.dto.OrderLineDishesRequest;
import org.mrshim.orderservice.dto.OrderPlacedEvent;
import org.mrshim.orderservice.exception.CreateOrderException;
import org.mrshim.orderservice.exception.OrderNotFoundException;
import org.mrshim.orderservice.model.Order;
import org.mrshim.orderservice.model.OrderLineDish;
import org.mrshim.orderservice.repository.OrderRepository;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
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

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClient;

    private final LoadBalancerClient loadBalancerClient;

    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;


    private boolean isInStockDishes(List<OrderLineDishesRequest> lineDishes)
    {
        ServiceInstance serviceInstance = loadBalancerClient.choose("menu-service");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lineDishes", lineDishes);

        String jsonString = jsonObject.toString();


        String serviceUrl = "http://localhost" + ":" + serviceInstance.getPort();

        Boolean block = webClient.baseUrl(serviceUrl)
                .build().post().uri("/menu/stock")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonString)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();


        return block;

    }




    private BigDecimal calculateSumOrder(List<OrderLineDishesRequest> lineDishes)
    {
        double a=0;

        for (int i = 0; i < lineDishes.size(); i++) {

            OrderLineDishesRequest orderLineDishesRequest = lineDishes.get(i);

           a+=orderLineDishesRequest.getPrice().doubleValue()*orderLineDishesRequest.getQuantity();

        }

        return new BigDecimal(a);


    }



    @Transactional
    public Long createOrder(CreateOrderRequest createOrderRequest) {
        Order order = new Order();

        if (!isInStockDishes(createOrderRequest.getLineDishes())) throw new CreateOrderException("Ошибка формирования заказа");


        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        Object userId = authentication.getToken().getClaim("sub");

        String userEmail = authentication.getToken().getClaim("email");

        String userName = authentication.getToken().getClaim("name");


        List<OrderLineDishesRequest> lineDishes = createOrderRequest.getLineDishes();

        BigDecimal sumOrder = calculateSumOrder(lineDishes);


        order.setContact(createOrderRequest.getContact());

        order.setStatus("Принят");
        order.setCreationDate(LocalDateTime.now());
        order.setUpdateDate(LocalDateTime.now());
        order.setUserId(userId.toString());
        order.setOrderAmount(sumOrder);
        order.setDeliveryAddress(createOrderRequest.getDeliveryAddress());
        order.setLineDishes(mapToOrderList(createOrderRequest.getLineDishes()));
        order.setPaymentMethod(createOrderRequest.getPaymentMethod());


        Order save = orderRepository.save(order);


        OrderPlacedEvent orderKafkaRequestDto = mapOrderToOrderKafkaRequest(save, userName, userEmail);

        kafkaTemplate.send("notificationTopic",orderKafkaRequestDto);

        return save.getId();


    }

    private OrderPlacedEvent mapOrderToOrderKafkaRequest(Order order, String name, String email)
    {

        OrderPlacedEvent orderKafkaRequestDto=new OrderPlacedEvent();
        orderKafkaRequestDto.setId(order.getId());
        orderKafkaRequestDto.setUserEmail(email);
        orderKafkaRequestDto.setUserName(name);
        orderKafkaRequestDto.setDeliveryAddress(orderKafkaRequestDto.getDeliveryAddress());
        orderKafkaRequestDto.setCreationDate(order.getCreationDate());
        orderKafkaRequestDto.setLineDishes(order.getLineDishes());
        orderKafkaRequestDto.setOrderAmount(order.getOrderAmount());



        return orderKafkaRequestDto;

    }

    public List<Order> getUserOderHistory()
    {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String userId = authentication.getToken().getClaim("sub");

        List<Order> allOrdersByUserId = orderRepository.findAllByUserId(userId);


        return allOrdersByUserId;



    }




    private List<OrderLineDish> mapToOrderList(List<OrderLineDishesRequest> orderLineDishesRequests) {
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
