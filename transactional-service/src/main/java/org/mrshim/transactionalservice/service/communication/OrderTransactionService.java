package org.mrshim.transactionalservice.service.communication;

import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.GetOrderRequestDto;
import org.mrshim.transactionalservice.dto.OrderResponse;
import org.mrshim.transactionalservice.exception.OrderNotFoundException;
import org.mrshim.transactionalservice.exception.OrderServiceException;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OrderTransactionService
{
    private final WebClient.Builder webClient;

    private final LoadBalancerClient loadBalancerClient;

    public Mono<OrderResponse> getOrder(Long id, String token) {

        ServiceInstance serviceInstance = loadBalancerClient.choose("order-service");
        GetOrderRequestDto getOrderRequestDto = new GetOrderRequestDto();
        getOrderRequestDto.setOrderId(id);

        if (serviceInstance==null)
        {
            throw new OrderServiceException("Извините, сервис временно недоступен");
        }

        String serviceUrl = "http://localhost" + ":" + serviceInstance.getPort();

        return webClient.baseUrl(serviceUrl).defaultHeader("Authorization", token)
                .build().get().uri("/order/" + id)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,clientResponse -> {
                    throw new OrderNotFoundException("Заказ не найден");
                })
                .bodyToMono(OrderResponse.class);
    }
}
