package com.epam.esm.epammodule3.config;

import com.epam.esm.epammodule3.util.GenerateInitialDataImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ContextStartedListener implements ApplicationListener<ContextStartedEvent> {

    private final GenerateInitialDataImpl generateInitialData;

    @Value("${initial-data.records}")
    private Integer numberOfRecords;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        generateInitialData.generate(numberOfRecords);
    }
}