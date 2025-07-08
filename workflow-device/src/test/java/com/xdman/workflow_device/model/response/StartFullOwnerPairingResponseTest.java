package com.xdman.workflow_device.model.response;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StartFullOwnerPairingResponse Tests")
class StartFullOwnerPairingResponseTest extends BaseTest {

    @Test
    @DisplayName("Should create StartFullOwnerPairingResponse with valid message")
    void shouldCreateStartFullOwnerPairingResponseWithValidMessage() {
        // Given
        String message = "Full owner pairing cycle started successfully";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

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
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

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
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals("", response.message());
    }

    @Test
    @DisplayName("Should be equal when messages are the same")
    void shouldBeEqualWhenMessagesAreSame() {
        // Given
        String message = "Success message";
        StartFullOwnerPairingResponse response1 = new StartFullOwnerPairingResponse(message);
        StartFullOwnerPairingResponse response2 = new StartFullOwnerPairingResponse(message);

        // When & Then
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when messages are different")
    void shouldNotBeEqualWhenMessagesAreDifferent() {
        // Given
        StartFullOwnerPairingResponse response1 = new StartFullOwnerPairingResponse("Success");
        StartFullOwnerPairingResponse response2 = new StartFullOwnerPairingResponse("Failure");

        // When & Then
        assertNotEquals(response1, response2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        String message = "Test success message";
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // When
        String toString = response.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("StartFullOwnerPairingResponse"));
        assertTrue(toString.contains(message));
    }

    @Test
    @DisplayName("Should handle success message")
    void shouldHandleSuccessMessage() {
        // Given
        String message = "SPAKE2+ full cycle completed successfully for VIN: " + TEST_VIN;

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("successfully"));
        assertTrue(response.message().contains(TEST_VIN));
    }

    @Test
    @DisplayName("Should handle error message")
    void shouldHandleErrorMessage() {
        // Given
        String message = "Error: Invalid password and salt format";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("Error"));
    }

    @Test
    @DisplayName("Should handle long message")
    void shouldHandleLongMessage() {
        // Given
        StringBuilder longMessage = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            longMessage.append("This is a very long message part ").append(i).append(". ");
        }
        String message = longMessage.toString();

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().length() > 1000);
    }

    @Test
    @DisplayName("Should handle message with special characters")
    void shouldHandleMessageWithSpecialCharacters() {
        // Given
        String message = "Success! @#$%^&*()_+-=[]{}|;':\",./<>?";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }

    @Test
    @DisplayName("Should handle message with newlines")
    void shouldHandleMessageWithNewlines() {
        // Given
        String message = "Line 1\nLine 2\nLine 3";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
        assertTrue(response.message().contains("\n"));
    }

    @Test
    @DisplayName("Should handle whitespace message")
    void shouldHandleWhitespaceMessage() {
        // Given
        String message = "   ";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals("   ", response.message());
    }

    @Test
    @DisplayName("Should handle Unicode characters")
    void shouldHandleUnicodeCharacters() {
        // Given
        String message = "成功启动完整所有者配对周期 🚗 🔑";

        // When
        StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);

        // Then
        assertNotNull(response);
        assertEquals(message, response.message());
    }
}
