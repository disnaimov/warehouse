package com.example.warehouse.filter;

import com.example.warehouse.config.CurrencyServiceConfig;
import com.example.warehouse.entities.Currency;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Profile("remote")
public class CurrencyFilter extends OncePerRequestFilter {
    private static final String CURRENCY_HEADER = "currency";
    private static final String DEFAULT_CURRENCY = "RUB";

    private final WebClient webClient;

    private final CurrencyServiceConfig currencyServiceConfig;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String selectedCurrency = request.getParameter("currency");
        if (selectedCurrency != null) {
            session.setAttribute("selectedCurrency", selectedCurrency);
        }

        webClient.get()
                .uri(currencyServiceConfig.getHost() + currencyServiceConfig.getMethods().get("get-currency"))
                .retrieve()
                .bodyToMono(Currency.class)
                .subscribe(currency -> {
                    session.setAttribute("currencyData", currency);
                });
        filterChain.doFilter(request, response);
    }
}
