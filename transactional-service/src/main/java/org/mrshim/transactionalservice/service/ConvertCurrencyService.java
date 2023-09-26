package org.mrshim.transactionalservice.service;

import java.math.BigDecimal;

public interface ConvertCurrencyService {


    double ConvertCurrency(BigDecimal orderAmount);
}
