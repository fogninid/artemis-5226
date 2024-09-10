package com.example.artemis5226;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "management.server.port=0",
        "management.endpoint.prometheus.enabled=true",
        "management.endpoints.web.exposure.include=prometheus"
})
@AutoConfigureObservability
class Artemis5226Tests {
    private static final Logger LOG = getLogger(Artemis5226Tests.class);

    @Autowired
    private RestTemplateBuilder template;

    @LocalManagementPort
    int managementPort;

    @Test
    void testPrometheus() {
        final ResponseEntity<String> response = template
                .rootUri("http://localhost:" + managementPort)
                .build()
                .getForEntity("/actuator/prometheus", String.class);
        LOG.info("response={}", response);

        assertThat(response.getBody())
                .contains("artemis_total_connection_count{broker=\"name-of-the-artemis-broker\"} 0.0")
                .doesNotContain("my_app{broker=\"name-of-the-artemis-broker\",label=\"mine\"} 42.0")
                .contains("my_app{label=\"mine\"} 42.0");
    }
}

