package com.example.warehouse.kafka;

import com.example.warehouse.hanlders.EventHandler;
import com.example.warehouse.request.EventSource;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Set;
@RequiredArgsConstructor
@Slf4j
@Service
public class Consumer {

    private final Set<EventHandler<EventSource>> eventHandlers;

    @KafkaListener(topics = "test_topic", containerFactory = "kafkaListenerContainerFactoryString")
    public void listenTestTopic(String message) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);

            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler not found"))
                    .handleEvent(eventSource);
        } catch (JsonProcessingException e) {
            log.error(message, e);
        }
    }
}
