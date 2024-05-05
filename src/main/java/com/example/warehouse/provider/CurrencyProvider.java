package com.example.warehouse.provider;

import com.example.warehouse.config.CurrencyServiceConfig;
import com.example.warehouse.entities.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
@Profile("remote")
public class CurrencyProvider {
    private final WebClient webClient;

    private final CurrencyServiceConfig currencyServiceConfig;

    public Mono<Currency> getCurrencies() {
        return webClient.get()
                .uri(currencyServiceConfig.getHost() + currencyServiceConfig.getMethods().get("get-currency"))
                .retrieve()
                .bodyToMono(Currency.class)
                .retry(2);
    }
}
