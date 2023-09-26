package org.mrshim.transactionalservice.service;

import reactor.core.publisher.Mono;

public interface CurrencyService {

    Mono<String> getCurrencyRates();


}
