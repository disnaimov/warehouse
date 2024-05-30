package com.example.warehouse.kafka;

import com.example.warehouse.hanlders.EventHandler;
import com.example.warehouse.request.EventSource;
import com.example.warehouse.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateOrderEvent implements EventHandler<CreateOrderEventData> {
    private final OrderService orderService;


    @Override
    public boolean canHandle(EventSource eventSource) {
        return Event.CREATE_ORDER.equals(eventSource.getEvent());
    }

    @Override
    public String handleEvent(EventSource eventSource) {
        orderService.create(eventSource);
    }
}
