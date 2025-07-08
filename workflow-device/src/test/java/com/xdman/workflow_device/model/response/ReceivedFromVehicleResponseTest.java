package com.xdman.workflow_device.model.response;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReceivedFromVehicleResponse Tests")
class ReceivedFromVehicleResponseTest extends BaseTest {

    @Test
    @DisplayName("Should create ReceivedFromVehicleResponse with valid message")
    void shouldCreateReceivedFromVehicleResponseWithValidMessage() {
        // Given
        String message = "Message received from vehicle successfully";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }

    @Test
    @DisplayName("Should handle null message")
    void shouldHandleNullMessage() {
        // Given
        String message = null;

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertNull(response.message());
    }

    @Test
    @DisplayName("Should handle empty message")
    void shouldHandleEmptyMessage() {
        // Given
        String message = "";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals("", response.message());
    }

    @Test
    @DisplayName("Should be equal when messages are the same")
    void shouldBeEqualWhenMessagesAreSame() {
        // Given
        String message = "Success message";
        ReceivedFromVehicleResponse response1 = new ReceivedFromVehicleResponse(message);
        ReceivedFromVehicleResponse response2 = new ReceivedFromVehicleResponse(message);

        // When & Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when messages are different")
    void shouldNotBeEqualWhenMessagesAreDifferent() {
        // Given
        ReceivedFromVehicleResponse response1 = new ReceivedFromVehicleResponse("Success");
        ReceivedFromVehicleResponse response2 = new ReceivedFromVehicleResponse("Failure");

        // When & Then
        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        String message = "Test response message";
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("ReceivedFromVehicleResponse"));
        assertTrue(toString.contains(message));
    }

    @Test
    @DisplayName("Should handle SPAKE2+ success message")
    void shouldHandleSpake2PlusSuccessMessage() {
        // Given
        String message = "SPAKE2+ for vehicle " + TEST_VIN + " started successfully!";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("SPAKE2+"));
        assertTrue(response.message().contains(TEST_VIN));
        assertTrue(response.message().contains("successfully"));
    }

    @Test
    @DisplayName("Should handle signal workflow message")
    void shouldHandleSignalWorkflowMessage() {
        // Given
        String message = "Received a message from vehicle successfully " + TEST_MESSAGE;

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Received a message"));
        assertTrue(response.message().contains(TEST_MESSAGE));
    }

    @Test
    @DisplayName("Should handle error message")
    void shouldHandleErrorMessage() {
        // Given
        String message = "Error: Invalid message format";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Error"));
    }

    @Test
    @DisplayName("Should handle hex message content")
    void shouldHandleHexMessageContent() {
        // Given
        String hexContent = createTestHexString(50);
        String message = "Processed hex message: " + hexContent;

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains(hexContent));
    }

    @Test
    @DisplayName("Should handle command header detection message")
    void shouldHandleCommandHeaderDetectionMessage() {
        // Given
        String message = "Select command detected, starting device owner pairing";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Select command detected"));
    }

    @Test
    @DisplayName("Should handle verify command detection message")
    void shouldHandleVerifyCommandDetectionMessage() {
        // Given
        String message = "Request or Verify command detected, signaling workflow";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Verify command detected"));
    }

    @Test
    @DisplayName("Should handle whitespace message")
    void shouldHandleWhitespaceMessage() {
        // Given
        String message = "   ";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals("   ", response.message());
    }

    @Test
    @DisplayName("Should handle special characters")
    void shouldHandleSpecialCharacters() {
        // Given
        String message = "Response: !@#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
        // Given
        String message = "从车辆成功接收消息 🚗 ✅";

        // When
        ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }
}
