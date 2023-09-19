package org.mrshim.deliveryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.deliveryservice.dto.RabbitMessageDto;
import org.mrshim.deliveryservice.model.Delivery;
import org.mrshim.deliveryservice.repository.DeliveryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final ObjectMapper objectMapper;



    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(String message)
    {

        try {
            RabbitMessageDto rabbitMessageDto = objectMapper.readValue(message, RabbitMessageDto.class);

            Delivery delivery=new Delivery();

            delivery.setDeliver(false);
            delivery.setOrder_id(rabbitMessageDto.getId());
            delivery.setDeliveryAddress(rabbitMessageDto.getDeliveryAddress());

            deliveryRepository.save(delivery);




        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }






}
