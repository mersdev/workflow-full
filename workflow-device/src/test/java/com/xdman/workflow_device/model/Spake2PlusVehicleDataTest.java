package com.xdman.workflow_device.model;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spake2PlusVehicleData Tests")
class Spake2PlusVehicleDataTest extends BaseTest {

    @Test
    @DisplayName("Should create Spake2PlusVehicleData with valid parameters")
    void shouldCreateSpake2PlusVehicleDataWithValidParameters() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertEquals(w0, vehicleData.w0());
        assertEquals(w1, vehicleData.w1());
    }

    @Test
    @DisplayName("Should handle null BigInteger values")
    void shouldHandleNullBigIntegerValues() {
        // Given
        BigInteger w0 = null;
        BigInteger w1 = null;

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertNull(vehicleData.w0());
        assertNull(vehicleData.w1());
    }

    @Test
    @DisplayName("Should be equal when all fields are the same")
    void shouldBeEqualWhenAllFieldsAreSame() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);

        Spake2PlusVehicleData vehicleData1 = new Spake2PlusVehicleData(w0, w1);
        Spake2PlusVehicleData vehicleData2 = new Spake2PlusVehicleData(w0, w1);

        // When & Then
        assertEquals(vehicleData1, vehicleData2);
        assertEquals(vehicleData1.hashCode(), vehicleData2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when w0 is different")
    void shouldNotBeEqualWhenW0IsDifferent() {
        // Given
        BigInteger w1 = BigInteger.valueOf(67890);

        Spake2PlusVehicleData vehicleData1 = new Spake2PlusVehicleData(BigInteger.valueOf(12345), w1);
        Spake2PlusVehicleData vehicleData2 = new Spake2PlusVehicleData(BigInteger.valueOf(54321), w1);

        // When & Then
        assertNotEquals(vehicleData1, vehicleData2);
    }

    @Test
    @DisplayName("Should not be equal when w1 is different")
    void shouldNotBeEqualWhenW1IsDifferent() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);

        Spake2PlusVehicleData vehicleData1 = new Spake2PlusVehicleData(w0, BigInteger.valueOf(67890));
        Spake2PlusVehicleData vehicleData2 = new Spake2PlusVehicleData(w0, BigInteger.valueOf(98765));

        // When & Then
        assertNotEquals(vehicleData1, vehicleData2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = BigInteger.valueOf(67890);

        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // When
        String toString = vehicleData.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("Spake2PlusVehicleData"));
    }

    @Test
    @DisplayName("Should handle large BigInteger values")
    void shouldHandleLargeBigIntegerValues() {
        // Given
        BigInteger w0 = new BigInteger("123456789012345678901234567890");
        BigInteger w1 = new BigInteger("987654321098765432109876543210");

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertEquals(w0, vehicleData.w0());
        assertEquals(w1, vehicleData.w1());
    }

    @Test
    @DisplayName("Should handle zero values")
    void shouldHandleZeroValues() {
        // Given
        BigInteger w0 = BigInteger.ZERO;
        BigInteger w1 = BigInteger.ZERO;

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertEquals(BigInteger.ZERO, vehicleData.w0());
        assertEquals(BigInteger.ZERO, vehicleData.w1());
    }

    @Test
    @DisplayName("Should handle negative values")
    void shouldHandleNegativeValues() {
        // Given
        BigInteger w0 = BigInteger.valueOf(-12345);
        BigInteger w1 = BigInteger.valueOf(-67890);

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertEquals(w0, vehicleData.w0());
        assertEquals(w1, vehicleData.w1());
    }

    @Test
    @DisplayName("Should handle mixed null and non-null values")
    void shouldHandleMixedNullAndNonNullValues() {
        // Given
        BigInteger w0 = BigInteger.valueOf(12345);
        BigInteger w1 = null;

        // When
        Spake2PlusVehicleData vehicleData = new Spake2PlusVehicleData(w0, w1);

        // Then
        assertNotNull(vehicleData);
        assertEquals(w0, vehicleData.w0());
        assertNull(vehicleData.w1());
    }
}
