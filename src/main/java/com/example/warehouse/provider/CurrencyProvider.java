package com.example.warehouse.provider;

import com.example.warehouse.entities.Currency;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@Data
@SessionScope
public class CurrencyProvider {
    private Currency currency;

    public CurrencyProvider() {
        this.currency = Currency.RUB;
    }
}
