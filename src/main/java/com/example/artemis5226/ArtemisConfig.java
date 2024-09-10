package com.example.artemis5226;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.activemq.artemis.core.config.MetricsConfiguration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.apache.activemq.artemis.core.server.metrics.ActiveMQMetricsPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ArtemisConfig {

    private final MeterRegistry meterRegistry;

    public ArtemisConfig(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    public EmbeddedActiveMQ embeddedActiveMQ() throws Exception {
        final EmbeddedActiveMQ embeddedActiveMQ = new EmbeddedActiveMQ();

        final ConfigurationImpl configuration = new ConfigurationImpl();
        configuration.setName("name-of-the-artemis-broker");
        configuration.addAcceptorConfiguration("internal", "vm://0");

        final MetricsConfiguration metricsConfiguration = new MetricsConfiguration();
        metricsConfiguration.setJvmMemory(false);
        metricsConfiguration.setPlugin(new ActiveMQMetricsPlugin() {
            @Override
            public ActiveMQMetricsPlugin init(Map<String, String> map) {
                return this;
            }

            @Override
            public MeterRegistry getRegistry() {
                return meterRegistry;
            }
        });
        configuration.setMetricsConfiguration(metricsConfiguration);

        embeddedActiveMQ.setConfiguration(configuration);

        return embeddedActiveMQ;

    }
}
