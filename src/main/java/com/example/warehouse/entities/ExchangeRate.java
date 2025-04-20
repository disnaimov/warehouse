package com.example.warehouse.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {
    @JsonProperty("CNY")
    private BigDecimal exchangeRateCNY;
    @JsonProperty("USD")
    private BigDecimal exchangeRateUSD;
    @JsonProperty("EUR")
    private BigDecimal exchangeRateEUR;
}
