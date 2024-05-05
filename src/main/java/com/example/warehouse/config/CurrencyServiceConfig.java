package com.example.warehouse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "currency-service")
@Getter
@Setter
@Profile("remote")
public class CurrencyServiceConfig {
    private String host;
    private Map<String, String> methods;
}
