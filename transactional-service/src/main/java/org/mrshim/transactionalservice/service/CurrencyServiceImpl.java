package org.mrshim.transactionalservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.CurrencyResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {

    private final WebClient.Builder webClient;

    private final ObjectMapper objectMapper;


    private final RedisTemplate<String, String> redisTemplate;

    public Mono<String> getCurrencyRates() {
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







}
