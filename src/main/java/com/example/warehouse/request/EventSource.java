package com.example.warehouse.request;

import com.example.warehouse.kafka.Event;

public interface EventSource {
    Event getEvent();
}
