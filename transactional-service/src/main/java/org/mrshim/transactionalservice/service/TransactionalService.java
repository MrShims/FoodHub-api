package org.mrshim.transactionalservice.service;

import org.mrshim.transactionalservice.dto.PaymentRequest;

public interface TransactionalService {
    String charge(PaymentRequest paymentRequest, String token);
}
