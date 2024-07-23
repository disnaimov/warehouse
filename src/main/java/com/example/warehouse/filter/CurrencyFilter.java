package com.example.warehouse.filter;

import com.example.warehouse.entities.Currency;
import com.example.warehouse.provider.CurrencyProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CurrencyFilter extends OncePerRequestFilter {

    private final CurrencyProvider currencyProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String currency = Currency.fromString(request.getHeader("Currency")).toString();
        Optional.ofNullable(currency)
                .map(Currency::valueOf)
                .ifPresent(currencyProvider::setCurrency);

        filterChain.doFilter(request, response);
    }
}
