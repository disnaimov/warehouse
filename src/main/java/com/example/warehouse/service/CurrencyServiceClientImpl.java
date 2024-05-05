package com.example.warehouse.service;

import com.example.warehouse.entities.Currency;
import com.example.warehouse.provider.CurrencyProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
@Profile("remote")
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient{
    private final CurrencyProvider currencyProvider;
    ObjectMapper objectMapper = new ObjectMapper();
    @Cacheable(value = "currencyCache", key = "#root.methodName")
    public Currency getWithCurrency() {
        Currency currency = new Currency();

        try {
            currency = currencyProvider.getCurrencies().block();
        } catch (Exception e) {
            try {
                log.info("Currency received from file exchange-rate.json");
                File file = new File("src/main/resources/exchange-rate.json");
                Currency fileCurrency = objectMapper.readValue(file, Currency.class);
                currency.setCNY(fileCurrency.getCNY());
                currency.setUSD(fileCurrency.getUSD());
                currency.setEUR(fileCurrency.getEUR());
            } catch (IOException ex) {
                log.error("Error reading currency data from file: " + ex.getMessage());
            }
        }

        return currency;
    }

    @CacheEvict(value = "currencyCache", allEntries = true)
    public void evictCache() {
    }
}
