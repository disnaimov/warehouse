package com.example.warehouse.service;

import com.example.warehouse.config.CurrencyServiceConfig;
import com.example.warehouse.entities.ExchangeRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Profile("remote")
@Service
@RequiredArgsConstructor
@Slf4j
public class CurrencyServiceClientImpl implements CurrencyServiceClient {
    private final WebClient webClient;
    private final CurrencyServiceConfig currencyServiceConfig;

    @Override
    public ExchangeRate getExchangeRate() {
        return webClient.get()
                .uri(currencyServiceConfig.getHost() + currencyServiceConfig.getMethods().get("get-currency"))
                .retrieve()
                .bodyToMono(ExchangeRate.class)
                .block();
    }
}
