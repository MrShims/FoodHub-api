package org.mrshim.transactionalservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.mrshim.transactionalservice.service.ConvertCurrencyService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
public class ConvertCurrencyServiceImpl implements ConvertCurrencyService {

    private final RedisTemplate<String, String> redisTemplate;

    public double ConvertCurrency(BigDecimal orderAmount) {
        String usd = redisTemplate.opsForValue().get("USD");
        assert usd != null;
        double v1 = Double.parseDouble(usd);

        return orderAmount.doubleValue() / v1 * 10000;
    }

}