package com.example.warehouse.kafka;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestConsume {

    @KafkaListener(topics = "test_topic", groupId = "group1")
    public void listen(String key, String message) {
        log.info(key, message);
    }
}
