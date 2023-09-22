package org.mrshim.transactionalservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.dto.CurrencyResponse;
import org.mrshim.transactionalservice.dto.PaymentDto;
import org.mrshim.transactionalservice.service.TransactionalService;
import org.springframework.web.bind.annotation.*;
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
    public Mono<String> startTransactional(/*@RequestParam(name = "id") Long id, @RequestBody String token,*/ @RequestHeader("Authorization") String authToken) {

return Mono.just(authToken);

      // return Mono.just(transactionalService.getCurrencyRate());

    }


}
