package com.example.warehouse.provider;

import com.example.warehouse.entities.Currency;
import com.example.warehouse.entities.ExchangeRate;
import com.example.warehouse.service.CurrencyServiceClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateProvider {

    private final CurrencyServiceClient currencyServiceClient;

    ObjectMapper objectMapper = new ObjectMapper();

    private @Nullable BigDecimal getExchangeRateFromService(Currency currency) {
        log.info("Получаем курс из второго сервиса или из кэша");
        try {
            return Optional.ofNullable(currencyServiceClient.getExchangeRate())
                    .map(rate -> getExchangeRateByCurrency(rate, currency)).orElse(null);
        } catch (Exception e) {
            log.error("Ошибка при получении курса из сервиса: {}", e.getMessage());
            return null;
        }
    }

    private BigDecimal getExchangeRateFromFile(Currency currency) throws IOException {
        log.info("Получаем курс из файла");
        File file = new File("src/main/resources/exchange-rate.json");
        try {
            ExchangeRate fileCurrency = objectMapper.readValue(file, ExchangeRate.class);
            return switch (currency) {
                case RUB -> BigDecimal.ONE;
                case CNY -> fileCurrency.getExchangeRateCNY();
                case USD -> fileCurrency.getExchangeRateUSD();
                case EUR -> fileCurrency.getExchangeRateEUR();
            };
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    private BigDecimal getExchangeRateByCurrency(ExchangeRate exchangeRate, Currency currency) {
        return switch (currency) {
            case RUB -> BigDecimal.ONE;
            case CNY -> exchangeRate.getExchangeRateCNY();
            case USD -> exchangeRate.getExchangeRateUSD();
            case EUR -> exchangeRate.getExchangeRateEUR();
        };
    }

    @Cacheable(value = "exchangeRates", key = "#currency", unless = "#result == null")
    public BigDecimal getExchangeRate(Currency currency) {
        return Optional.ofNullable(getExchangeRateFromService(currency))
                .orElseGet(() -> {
                    try {
                        return getExchangeRateFromFile(currency);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
