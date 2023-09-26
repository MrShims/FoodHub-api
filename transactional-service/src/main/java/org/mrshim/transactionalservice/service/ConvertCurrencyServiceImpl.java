package org.mrshim.transactionalservice.service;

import lombok.RequiredArgsConstructor;
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


        double price = orderAmount.doubleValue() / v1 * 10000;


        return price;


    }


}