package com.xdman.workflow_device.model.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SendToVehicleRequest Tests")
class SendToVehicleRequestTest extends BaseTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Should create SendToVehicleRequest with valid message payload")
    void shouldCreateSendToVehicleRequestWithValidMessagePayload() {
        // Given
        String messagePayload = TEST_MESSAGE;

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(messagePayload);

        // Then
        assertNotNull(request);
        assertEquals(messagePayload, request.messagePayload());
    }

    @Test
    @DisplayName("Should handle null message payload")
    void shouldHandleNullMessagePayload() {
        // Given
        String messagePayload = null;

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(messagePayload);

        // Then
        assertNotNull(request);
        assertNull(request.messagePayload());
    }

    @Test
    @DisplayName("Should handle empty message payload")
    void shouldHandleEmptyMessagePayload() {
        // Given
        String messagePayload = "";

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(messagePayload);

        // Then
        assertNotNull(request);
        assertEquals("", request.messagePayload());
    }

    @Test
    @DisplayName("Should be equal when message payloads are the same")
    void shouldBeEqualWhenMessagePayloadsAreSame() {
        // Given
        SendToVehicleRequest request1 = new SendToVehicleRequest(TEST_MESSAGE);
        SendToVehicleRequest request2 = new SendToVehicleRequest(TEST_MESSAGE);

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when message payloads are different")
    void shouldNotBeEqualWhenMessagePayloadsAreDifferent() {
        // Given
        SendToVehicleRequest request1 = new SendToVehicleRequest(TEST_MESSAGE);
        SendToVehicleRequest request2 = new SendToVehicleRequest("differentMessage");

        // When & Then
        assertNotEquals(request1, request2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("SendToVehicleRequest"));
        assertTrue(toString.contains(TEST_MESSAGE));
    }

    @Test
    @DisplayName("Should serialize to JSON correctly")
    void shouldSerializeToJsonCorrectly() throws Exception {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertNotNull(json);
        assertTrue(json.contains("\"message\""));
        assertTrue(json.contains(TEST_MESSAGE));
    }

    @Test
    @DisplayName("Should deserialize from JSON correctly")
    void shouldDeserializeFromJsonCorrectly() throws Exception {
        // Given
        String json = "{\"message\":\"" + TEST_MESSAGE + "\"}";

        // When
        SendToVehicleRequest request = objectMapper.readValue(json, SendToVehicleRequest.class);

        // Then
        assertNotNull(request);
        assertEquals(TEST_MESSAGE, request.messagePayload());
    }

    @Test
    @DisplayName("Should handle JSON property annotation")
    void shouldHandleJsonPropertyAnnotation() throws Exception {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertNotNull(json);
        // The @JsonProperty annotation should map messagePayload to "message" in JSON
        assertTrue(json.contains("\"message\""));
        assertFalse(json.contains("\"messagePayload\""));
    }

    @Test
    @DisplayName("Should handle null message payload in JSON")
    void shouldHandleNullMessagePayloadInJson() throws Exception {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(null);

        // When
        String json = objectMapper.writeValueAsString(request);

        // Then
        assertNotNull(json);
        assertTrue(json.contains("\"message\":null") || json.contains("\"message\": null"));
    }

    @Test
    @DisplayName("Should handle hex message payload")
    void shouldHandleHexMessagePayload() {
        // Given
        String hexMessage = createTestHexString(100);

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(hexMessage);

        // Then
        assertNotNull(request);
        assertEquals(hexMessage, request.messagePayload());
        assertEquals(200, request.messagePayload().length()); // createTestHexString(100) creates 200 characters
    }

    @Test
    @DisplayName("Should handle whitespace in message payload")
    void shouldHandleWhitespaceInMessagePayload() {
        // Given
        String messagePayload = "  " + TEST_MESSAGE + "  ";

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(messagePayload);

        // Then
        assertNotNull(request);
        assertEquals(messagePayload, request.messagePayload());
    }

    @Test
    @DisplayName("Should handle special characters in message payload")
    void shouldHandleSpecialCharactersInMessagePayload() {
        // Given
        String messagePayload = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        SendToVehicleRequest request = new SendToVehicleRequest(messagePayload);

        // Then
        assertNotNull(request);
        assertEquals(messagePayload, request.messagePayload());
    }
}
