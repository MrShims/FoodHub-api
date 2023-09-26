package org.mrshim.transactionalservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Charge;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.CurrencyResponse;
import org.mrshim.transactionalservice.dto.PaymentRequest;
import org.mrshim.transactionalservice.service.TransactionalService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactional")
public class TransactionalController {

    private final TransactionalService transactionalService;






    @PostMapping("/charge")
    public Mono<String> startTransactional(@RequestHeader("Authorization") String authToken, @RequestBody PaymentRequest paymentRequest) {


        CompletableFuture<String> future=CompletableFuture.supplyAsync(()->

                        transactionalService.charge(paymentRequest,authToken)
                );

       return Mono.fromFuture(future);


    }


}
