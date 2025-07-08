package com.xdman.workflow_device.model.request;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReceivedFromVehicleRequest Tests")
class ReceivedFromVehicleRequestTest extends BaseTest {

    @Test
    @DisplayName("Should create ReceivedFromVehicleRequest with valid message")
    void shouldCreateReceivedFromVehicleRequestWithValidMessage() {
        // Given
        String message = TEST_MESSAGE;

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
    }

    @Test
    @DisplayName("Should handle null message")
    void shouldHandleNullMessage() {
        // Given
        String message = null;

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertNull(request.message());
    }

    @Test
    @DisplayName("Should handle empty message")
    void shouldHandleEmptyMessage() {
        // Given
        String message = "";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals("", request.message());
    }

    @Test
    @DisplayName("Should handle whitespace message")
    void shouldHandleWhitespaceMessage() {
        // Given
        String message = "   ";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals("   ", request.message());
    }

    @Test
    @DisplayName("Should be equal when messages are the same")
    void shouldBeEqualWhenMessagesAreSame() {
        // Given
        ReceivedFromVehicleRequest request1 = new ReceivedFromVehicleRequest(TEST_MESSAGE);
        ReceivedFromVehicleRequest request2 = new ReceivedFromVehicleRequest(TEST_MESSAGE);

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when messages are different")
    void shouldNotBeEqualWhenMessagesAreDifferent() {
        // Given
        ReceivedFromVehicleRequest request1 = new ReceivedFromVehicleRequest(TEST_MESSAGE);
        ReceivedFromVehicleRequest request2 = new ReceivedFromVehicleRequest("differentMessage");

        // When & Then
        assertNotEquals(request1, request2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(TEST_MESSAGE);

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("ReceivedFromVehicleRequest"));
        assertTrue(toString.contains(TEST_MESSAGE));
    }

    @Test
    @DisplayName("Should handle SELECT command header in message")
    void shouldHandleSelectCommandHeaderInMessage() {
        // Given
        String message = SELECT_COMMAND_HEADER + "additional_data";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
        assertTrue(request.message().contains(SELECT_COMMAND_HEADER));
    }

    @Test
    @DisplayName("Should handle REQUEST command header in message")
    void shouldHandleRequestCommandHeaderInMessage() {
        // Given
        String message = REQUEST_COMMAND_HEADER + "additional_data";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
        assertTrue(request.message().contains(REQUEST_COMMAND_HEADER));
    }

    @Test
    @DisplayName("Should handle VERIFY command header in message")
    void shouldHandleVerifyCommandHeaderInMessage() {
        // Given
        String message = VERIFY_COMMAND_HEADER + "additional_data";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
        assertTrue(request.message().contains(VERIFY_COMMAND_HEADER));
    }

    @Test
    @DisplayName("Should handle long hex message")
    void shouldHandleLongHexMessage() {
        // Given
        String message = createTestHexString(200);

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
        assertEquals(400, request.message().length()); // createTestHexString(200) creates 400 characters
    }

    @Test
    @DisplayName("Should handle message with special characters")
    void shouldHandleMessageWithSpecialCharacters() {
        // Given
        String message = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
    }

    @Test
    @DisplayName("Should handle Unicode characters in message")
    void shouldHandleUnicodeCharactersInMessage() {
        // Given
        String message = "测试消息 🚗 🔑";

        // When
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(message);

        // Then
        assertNotNull(request);
        assertEquals(message, request.message());
    }
}
