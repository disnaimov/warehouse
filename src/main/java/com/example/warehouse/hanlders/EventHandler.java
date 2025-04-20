package com.example.warehouse.hanlders;

import com.example.warehouse.request.EventSource;

public interface EventHandler <T extends EventSource> {
    boolean canHandle(EventSource eventSource);
    String handleEvent(T eventSource);
}
