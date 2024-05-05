package com.example.warehouse.config;

import com.example.warehouse.exceptions.InvalidEntityDataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
@Profile("remote")
public class WebClientConfig {
    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, CurrencyServiceConfig currencyServiceConfig) {
        return webClientBuilder.baseUrl(currencyServiceConfig.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new InvalidEntityDataException("Error", errorBody, (HttpStatus) clientResponse.statusCode())));
                    }
                    return Mono.just(clientResponse);
                }))
                .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
                    log.info("Executing request to: {}", clientRequest.url());
                    return Mono.just(clientRequest);
                }))
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .codecs(configurer  -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }
}
