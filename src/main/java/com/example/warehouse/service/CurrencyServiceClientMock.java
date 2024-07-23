package com.example.warehouse.service;

import com.example.warehouse.entities.ExchangeRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Profile("default")
@Service
@Slf4j
public class CurrencyServiceClientMock implements CurrencyServiceClient {
    @Override
    public ExchangeRate getExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();

        Random random = new Random();
        BigDecimal CNY = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to CNY is " + CNY);

        BigDecimal USD = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to USD is " + USD);

        BigDecimal EUR = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to EUR is " + EUR);

        exchangeRate.setExchangeRateCNY(CNY);
        exchangeRate.setExchangeRateUSD(USD);
        exchangeRate.setExchangeRateEUR(EUR);

        return exchangeRate;
    }
}
