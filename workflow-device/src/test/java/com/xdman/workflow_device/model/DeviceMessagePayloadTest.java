package com.xdman.workflow_device.model;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DeviceMessagePayload Tests")
class DeviceMessagePayloadTest extends BaseTest {

    @Test
    @DisplayName("Should create DeviceMessagePayload with valid VIN and message")
    void shouldCreateDeviceMessagePayloadWithValidData() {
        // Given
        String vin = TEST_VIN;
        String message = TEST_MESSAGE;

        // When
        DeviceMessagePayload payload = new DeviceMessagePayload(vin, message);

        // Then
        assertNotNull(payload);
        assertEquals(vin, payload.vin());
        assertEquals(message, payload.message());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is null")
    void shouldThrowExceptionWhenVinIsNull() {
        // Given
        String vin = null;
        String message = TEST_MESSAGE;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("VIN cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is blank")
    void shouldThrowExceptionWhenVinIsBlank() {
        // Given
        String vin = "   ";
        String message = TEST_MESSAGE;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("VIN cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when VIN is empty")
    void shouldThrowExceptionWhenVinIsEmpty() {
        // Given
        String vin = "";
        String message = TEST_MESSAGE;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("VIN cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when message is null")
    void shouldThrowExceptionWhenMessageIsNull() {
        // Given
        String vin = TEST_VIN;
        String message = null;

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("Message cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when message is blank")
    void shouldThrowExceptionWhenMessageIsBlank() {
        // Given
        String vin = TEST_VIN;
        String message = "   ";

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("Message cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when message is empty")
    void shouldThrowExceptionWhenMessageIsEmpty() {
        // Given
        String vin = TEST_VIN;
        String message = "";

        // When & Then
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new DeviceMessagePayload(vin, message)
        );
        assertEquals("Message cannot be null or blank", exception.getMessage());
    }

    @Test
    @DisplayName("Should be equal when VIN and message are the same")
    void shouldBeEqualWhenDataIsSame() {
        // Given
        DeviceMessagePayload payload1 = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);
        DeviceMessagePayload payload2 = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);

        // When & Then
        assertEquals(payload1, payload2);
        assertEquals(payload1.hashCode(), payload2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when VIN is different")
    void shouldNotBeEqualWhenVinIsDifferent() {
        // Given
        DeviceMessagePayload payload1 = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);
        DeviceMessagePayload payload2 = new DeviceMessagePayload("DIFFERENT_VIN", TEST_MESSAGE);

        // When & Then
        assertNotEquals(payload1, payload2);
    }

    @Test
    @DisplayName("Should not be equal when message is different")
    void shouldNotBeEqualWhenMessageIsDifferent() {
        // Given
        DeviceMessagePayload payload1 = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);
        DeviceMessagePayload payload2 = new DeviceMessagePayload(TEST_VIN, "DIFFERENT_MESSAGE");

        // When & Then
        assertNotEquals(payload1, payload2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        DeviceMessagePayload payload = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);

        // When
        String toString = payload.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains(TEST_VIN));
        assertTrue(toString.contains(TEST_MESSAGE));
        assertTrue(toString.contains("DeviceMessagePayload"));
    }
}
