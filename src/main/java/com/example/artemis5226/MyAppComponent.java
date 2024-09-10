package com.example.artemis5226;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.springframework.stereotype.Component;

@Component
public class MyAppComponent {
    public MyAppComponent(final MeterRegistry meterRegistry,
                          // unused, only for ordering
                          final EmbeddedActiveMQ embeddedActiveMQ) {
        meterRegistry.gauge("my.app", Tags.of("label", "mine"), this, MyAppComponent::getSize);
    }

    int getSize() {
        return 42;
    }
}
