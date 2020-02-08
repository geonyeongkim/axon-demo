package com.cqrs.query.configuration;

import com.cqrs.common.interceptor.MyEventHandlerInterceptor;
import org.axonframework.config.Configurer;
import org.axonframework.eventhandling.TrackingEventProcessorConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryConfiguration {

    @Autowired
    public void configureEventProcessing(Configurer configurer) {
        configurer.eventProcessing()
                .registerTrackingEventProcessor("accounts",
                        org.axonframework.config.Configuration::eventStore,
                        c -> TrackingEventProcessorConfiguration.forSingleThreadedProcessing()
                        .andBatchSize(1))
                .registerHandlerInterceptor("accounts",
                        configuration -> new MyEventHandlerInterceptor());
    }
}
