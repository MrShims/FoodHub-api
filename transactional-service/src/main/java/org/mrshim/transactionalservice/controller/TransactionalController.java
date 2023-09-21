package org.mrshim.transactionalservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.CurrencyResponse;
import org.mrshim.transactionalservice.dto.PaymentDto;
import org.mrshim.transactionalservice.service.TransactionalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactional")
public class TransactionalController {

    private final TransactionalService transactionalService;






    @PostMapping("/charge")
    public Mono<String> startTransactional() {



       return Mono.just(transactionalService.getCurrencyRate());

    }


}
