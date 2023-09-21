package org.mrshim.deliveryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.deliveryservice.dto.AvailableOrdersResponseDto;
import org.mrshim.deliveryservice.dto.RabbitMessageDto;
import org.mrshim.deliveryservice.model.Delivery;
import org.mrshim.deliveryservice.repository.DeliveryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final ObjectMapper objectMapper;




    public List<AvailableOrdersResponseDto> getAvailableOrders()
    {

      List<Delivery> allByCourierIdIsNull = deliveryRepository.findAllByCourierIdIsNull();

       return allByCourierIdIsNull.stream().map(this::mapDeliveryToAvailableOrders).toList();




    }

    public List<AvailableOrdersResponseDto> getHistory()
    {
        JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String courierId = authentication.getToken().getClaim("sub");

        List<Delivery> allByCourierId = deliveryRepository.findAllByCourierId(courierId);

        return allByCourierId.stream().map(this::mapDeliveryToAvailableOrders).toList();


    }


    private AvailableOrdersResponseDto mapDeliveryToAvailableOrders(Delivery delivery)
    {
        AvailableOrdersResponseDto availableOrdersResponseDto=new AvailableOrdersResponseDto();
        availableOrdersResponseDto.setAddress(delivery.getDeliveryAddress());
        availableOrdersResponseDto.setId(delivery.getOrderId());

        return availableOrdersResponseDto;




    }

    public void AcceptOrder(Long id)
    {

        Optional<Delivery> byOrderId = deliveryRepository.findByOrderId(id);

        if (byOrderId.isPresent())
        {



            Delivery delivery = byOrderId.get();

            JwtAuthenticationToken authentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            String userId = authentication.getToken().getClaim("sub");

            delivery.setCourierId(userId);

        }

        throw new RuntimeException();


    }



    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(String message)
    {

        try {
            RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);

            Delivery delivery=new Delivery();

            delivery.setDeliver(false);
            delivery.setOrderId(rabbitMessageDto.getId());
            delivery.setDeliveryAddress(rabbitMessageDto.getDeliveryAddress());

            deliveryRepository.save(delivery);




        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }


    public void deliveryOrder(Long orderId) {

        Optional<Delivery> byOrderId = deliveryRepository.findByOrderId(orderId);

        if (byOrderId.isPresent())
        {
            Delivery delivery = byOrderId.get();

            delivery.setDeliver(true);

            deliveryRepository.save(delivery);


        }
        else throw new RuntimeException();


    }
}
