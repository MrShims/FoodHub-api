package org.mrshim.transactionalservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import com.stripe.param.TokenCreateParams;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.CurrencyResponse;
import org.mrshim.transactionalservice.dto.GetOrderRequestDto;
import org.mrshim.transactionalservice.dto.PaymentDto;
import org.mrshim.transactionalservice.stripe.StripeClient;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionalService {

    private final WebClient.Builder webClient;

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, String> redisTemplate;

    private final LoadBalancerClient loadBalancerClient;

    private final StripeClient stripeClient;


    private Mono<String> getCurrencyRates() {
        return webClient.baseUrl("https://www.cbr-xml-daily.ru/daily_json.js")
                .build()
                .get()
                .retrieve()
                .bodyToMono(String.class)
                .flatMap(block -> {
                    try {
                        CurrencyResponse currency = objectMapper.readValue(block, CurrencyResponse.class);

                        Map.Entry<String, CurrencyResponse.Currency> usd = currency.getValute().entrySet().stream()
                                .filter(e -> e.getKey().equals("USD"))
                                .findFirst()
                                .orElse(null);

                        if (usd != null) {
                            return Mono.fromSupplier(() -> String.valueOf(usd.getValue().getValue()));
                        } else {
                            return Mono.just("USD not found");
                        }
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                })
                .onErrorResume(e -> Mono.just("Error: " + e.getMessage()));

    }


    @Scheduled(initialDelay = 0, fixedDelay = 1800000)
    public void saveCurrencyRates() {
        String currencyRates = getCurrencyRates().block();


        assert currencyRates != null;
        redisTemplate.opsForValue().set("USD", currencyRates);


    }

    public String getCurrencyRate() {


        String usd = redisTemplate.opsForValue().get("USD");

        return usd;


    }


    public String charge(Long id, String token) {


        Mono<PaymentDto> order = getOrder(id, token);
        PaymentDto block = order.block();

        double v = block.getOrderAmount().doubleValue();

        String usd = redisTemplate.opsForValue().get("USD");

        double v1 = Double.parseDouble(usd);


        double price = v/v1*10000;


        try {
            return stripeClient.chargeNewCard("tok_visa",price).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private Mono<PaymentDto> getOrder(Long id,String token) {

        ServiceInstance serviceInstance = loadBalancerClient.choose("order-service");
        GetOrderRequestDto getOrderRequestDto = new GetOrderRequestDto();
        getOrderRequestDto.setOrderId(id);


        String serviceUrl = "http://localhost" + ":" + serviceInstance.getPort();

       return  webClient.baseUrl(serviceUrl).defaultHeader("Authorization", token)
                .build().get().uri("/order/" + id)
                .retrieve()
                .bodyToMono(PaymentDto.class);





    }


}
