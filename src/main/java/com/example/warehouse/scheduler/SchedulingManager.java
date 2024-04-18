package com.example.warehouse.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SchedulingManager {

    private final OptimizedScheduler optimizedScheduler;

    private final SimpleScheduler simpleScheduler;

    @Value("${app.scheduling.enabled}")
    private boolean isSchedulingEnabled;

    @Value("${app.scheduling.optimization}")
    private boolean isOptimizationEnabled;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Scheduled(fixedDelay = 3000L)
    public void createSchedulers() {
        if (isSchedulingEnabled && !activeProfile.equals("develop")) {
            if (isOptimizationEnabled) {
                optimizedScheduler.batchUpdateTest();
            } else {
                simpleScheduler.updatePrice();
            }
        }
    }


}
