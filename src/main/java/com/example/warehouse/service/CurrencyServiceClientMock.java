package com.example.warehouse.service;

import com.example.warehouse.entities.Currency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Profile("default")
@Service
@Slf4j
public class CurrencyServiceClientMock implements CurrencyServiceClient{
    @Cacheable(value = "currencyCache", key = "#root.methodName")
    public Currency getWithCurrency() {
        Random random = new Random();
        BigDecimal CNY = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to CNY is " + CNY);

        BigDecimal USD = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to USD is " + USD);

        BigDecimal EUR = BigDecimal.valueOf(random.nextDouble(100) + 1);
        log.info("Random value to EUR is " + EUR);

        Currency currency = new Currency();
        currency.setCNY(CNY);
        currency.setUSD(USD);
        currency.setEUR(EUR);

        return currency;
    }

    @CacheEvict(value = "currencyCache", allEntries = true)
    public void evictCache() {
    }
}
