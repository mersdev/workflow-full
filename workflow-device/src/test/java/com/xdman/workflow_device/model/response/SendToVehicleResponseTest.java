package com.xdman.workflow_device.model.response;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SendToVehicleResponse Tests")
class SendToVehicleResponseTest extends BaseTest {

    @Test
    @DisplayName("Should create SendToVehicleResponse with valid message")
    void shouldCreateSendToVehicleResponseWithValidMessage() {
        // Given
        String message = "Message sent to vehicle successfully";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

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
        SendToVehicleResponse response = new SendToVehicleResponse(message);

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
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals("", response.message());
    }

    @Test
    @DisplayName("Should be equal when messages are the same")
    void shouldBeEqualWhenMessagesAreSame() {
        // Given
        String message = "Success message";
        SendToVehicleResponse response1 = new SendToVehicleResponse(message);
        SendToVehicleResponse response2 = new SendToVehicleResponse(message);

        // When & Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when messages are different")
    void shouldNotBeEqualWhenMessagesAreDifferent() {
        // Given
        SendToVehicleResponse response1 = new SendToVehicleResponse("Success");
        SendToVehicleResponse response2 = new SendToVehicleResponse("Failure");

        // When & Then
        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        String message = "Test response message";
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("SendToVehicleResponse"));
        assertTrue(toString.contains(message));
    }

    @Test
    @DisplayName("Should handle success message from DKC client")
    void shouldHandleSuccessMessageFromDkcClient() {
        // Given
        String message = "Command message sent to vehicle " + TEST_VIN + " successfully";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains(TEST_VIN));
        assertTrue(response.message().contains("successfully"));
    }

    @Test
    @DisplayName("Should handle fallback message")
    void shouldHandleFallbackMessage() {
        // Given
        String message = "Fallback response: Unable to send message to vehicle with VIN " + TEST_VIN + ". Cause: Connection timeout";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Fallback response"));
        assertTrue(response.message().contains(TEST_VIN));
        assertTrue(response.message().contains("Connection timeout"));
    }

    @Test
    @DisplayName("Should handle error message")
    void shouldHandleErrorMessage() {
        // Given
        String message = "Error: Failed to send message to vehicle";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

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
        String message = "Sent hex message: " + hexContent;

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains(hexContent));
    }

    @Test
    @DisplayName("Should handle timeout message")
    void shouldHandleTimeoutMessage() {
        // Given
        String message = "Request timeout: Vehicle did not respond within expected time";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("timeout"));
    }

    @Test
    @DisplayName("Should handle network error message")
    void shouldHandleNetworkErrorMessage() {
        // Given
        String message = "Network error: Unable to reach vehicle service at localhost:3031";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Network error"));
        assertTrue(response.message().contains("localhost:3031"));
    }

    @Test
    @DisplayName("Should handle whitespace message")
    void shouldHandleWhitespaceMessage() {
        // Given
        String message = "   ";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

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
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
        // Given
        String message = "消息已成功发送到车辆 🚗 📤";

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }

    @Test
    @DisplayName("Should handle long message")
    void shouldHandleLongMessage() {
        // Given
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            longMessage.append("This is a long response message part ").append(i).append(". ");
        }
        String message = longMessage.toString();

        // When
        SendToVehicleResponse response = new SendToVehicleResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().length() > 500);
    }
}
