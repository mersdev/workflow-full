package com.xdman.workflow_vehicle.model;

import com.xdman.workflow_vehicle.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spake2PlusDeviceData Tests - Vehicle Repository")
class Spake2PlusDeviceDataTest extends BaseTest {

    @Test
    @DisplayName("Should create Spake2PlusDeviceData with valid parameters")
    void shouldCreateSpake2PlusDeviceDataWithValidParameters() {
        // Given
        String password = TEST_PASSWORD;
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);
        BigInteger x = BigInteger.valueOf(11111);

        // When
        Spake2PlusDeviceData deviceData = new Spake2PlusDeviceData(password, w0, w1, x);

        // Then
        assertNotNull(deviceData);
        assertEquals(password, deviceData.password());
        assertEquals(w0, deviceData.w0());
        assertEquals(w1, deviceData.w1());
        assertEquals(x, deviceData.x());
    }

    @Test
    @DisplayName("Should handle null password")
    void shouldHandleNullPassword() {
        // Given
        String password = null;
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);
        BigInteger x = BigInteger.valueOf(11111);

        // When
        Spake2PlusDeviceData deviceData = new Spake2PlusDeviceData(password, w0, w1, x);

        // Then
        assertNotNull(deviceData);
        assertNull(deviceData.password());
        assertEquals(w0, deviceData.w0());
        assertEquals(w1, deviceData.w1());
        assertEquals(x, deviceData.x());
    }

    @Test
    @DisplayName("Should handle null BigInteger values")
    void shouldHandleNullBigIntegerValues() {
        // Given
        String password = TEST_PASSWORD;
        BigInteger w0 = null;
        BigInteger w1 = null;
        BigInteger x = null;

        // When
        Spake2PlusDeviceData deviceData = new Spake2PlusDeviceData(password, w0, w1, x);

        // Then
        assertNotNull(deviceData);
        assertEquals(password, deviceData.password());
        assertNull(deviceData.w0());
        assertNull(deviceData.w1());
        assertNull(deviceData.x());
    }

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreSame() {
        // Given
        String password = TEST_PASSWORD;
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);
        BigInteger x = BigInteger.valueOf(11111);

        Spake2PlusDeviceData deviceData1 = new Spake2PlusDeviceData(password, w0, w1, x);
        Spake2PlusDeviceData deviceData2 = new Spake2PlusDeviceData(password, w0, w1, x);

        // When & Then
        assertEquals(deviceData1, deviceData2);
        assertEquals(deviceData1.hashCode(), deviceData2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when password is different")
    void shouldNotBeEqualWhenPasswordIsDifferent() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);
        BigInteger x = BigInteger.valueOf(11111);

        Spake2PlusDeviceData deviceData1 = new Spake2PlusDeviceData(TEST_PASSWORD, w0, w1, x);
        Spake2PlusDeviceData deviceData2 = new Spake2PlusDeviceData("differentPassword", w0, w1, x);

        // When & Then
        assertNotEquals(deviceData1, deviceData2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        String password = TEST_PASSWORD;
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);
        BigInteger x = BigInteger.valueOf(11111);

        Spake2PlusDeviceData deviceData = new Spake2PlusDeviceData(password, w0, w1, x);

        // When
        String toString = deviceData.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Spake2PlusDeviceData"));
    }

    @Test
    @DisplayName("Should handle large BigInteger values")
    void shouldHandleLargeBigIntegerValues() {
        // Given
        String password = TEST_PASSWORD;
        BigInteger w0 = new BigInteger("123456789012345678901234567890");
        BigInteger w1 = new BigInteger("987654321098765432109876543210");
        BigInteger x = new BigInteger("555666777888999000111222333444");

        // When
        Spake2PlusDeviceData deviceData = new Spake2PlusDeviceData(password, w0, w1, x);

        // Then
        assertNotNull(deviceData);
        assertEquals(password, deviceData.password());
        assertEquals(w0, deviceData.w0());
        assertEquals(w1, deviceData.w1());
        assertEquals(x, deviceData.x());
    }
}
