package com.xdman.workflow_device.base;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

/**
 * Base integration test class for tests that require full Spring context
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest extends BaseTest {

    @LocalServerPort
    protected int port;

    protected String baseUrl;

    @BeforeEach
    @Override
    protected void setUp() {
        super.setUp();
        baseUrl = "http://localhost:" + port;
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Configure test properties dynamically
        registry.add("spring.temporal.connection.target", () -> "localhost:7234");
        registry.add("test.external-services.vehicle-service.enabled", () -> "false");
    }

    /**
     * Helper method to get full URL for endpoint
     */
    protected String getUrl(String endpoint) {
        return baseUrl + endpoint;
    }
}
