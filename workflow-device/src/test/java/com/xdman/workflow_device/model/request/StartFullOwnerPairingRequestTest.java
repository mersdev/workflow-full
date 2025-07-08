package com.xdman.workflow_device.model.request;

import com.xdman.workflow_device.base.BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("StartFullOwnerPairingRequest Tests")
class StartFullOwnerPairingRequestTest extends BaseTest {

    @Test
    @DisplayName("Should create StartFullOwnerPairingRequest with valid parameters")
    void shouldCreateStartFullOwnerPairingRequestWithValidParameters() {
        // Given
        String password = TEST_PASSWORD;
        String salt = TEST_SALT;

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertEquals(salt, request.salt());
    }

    @Test
    @DisplayName("Should handle null password")
    void shouldHandleNullPassword() {
        // Given
        String password = null;
        String salt = TEST_SALT;

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertNull(request.password());
        assertEquals(salt, request.salt());
    }

    @Test
    @DisplayName("Should handle null salt")
    void shouldHandleNullSalt() {
        // Given
        String password = TEST_PASSWORD;
        String salt = null;

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertNull(request.salt());
    }

    @Test
    @DisplayName("Should handle empty password")
    void shouldHandleEmptyPassword() {
        // Given
        String password = "";
        String salt = TEST_SALT;

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals("", request.password());
        assertEquals(salt, request.salt());
    }

    @Test
    @DisplayName("Should handle empty salt")
    void shouldHandleEmptySalt() {
        // Given
        String password = TEST_PASSWORD;
        String salt = "";

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertEquals("", request.salt());
    }

    @Test
    @DisplayName("Should be equal when password and salt are the same")
    void shouldBeEqualWhenPasswordAndSaltAreSame() {
        // Given
        StartFullOwnerPairingRequest request1 = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);
        StartFullOwnerPairingRequest request2 = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);

        // When & Then
        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when password is different")
    void shouldNotBeEqualWhenPasswordIsDifferent() {
        // Given
        StartFullOwnerPairingRequest request1 = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);
        StartFullOwnerPairingRequest request2 = new StartFullOwnerPairingRequest("differentPassword", TEST_SALT);

        // When & Then
        assertNotEquals(request1, request2);
    }

    @Test
    @DisplayName("Should not be equal when salt is different")
    void shouldNotBeEqualWhenSaltIsDifferent() {
        // Given
        StartFullOwnerPairingRequest request1 = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);
        StartFullOwnerPairingRequest request2 = new StartFullOwnerPairingRequest(TEST_PASSWORD, "differentSalt");

        // When & Then
        assertNotEquals(request1, request2);
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);

        // When
        String toString = request.toString();

        // Then
        assertNotNull(toString);
        assertTrue(toString.contains("StartFullOwnerPairingRequest"));
        // Note: password might be masked in toString for security
    }

    @Test
    @DisplayName("Should handle whitespace in password and salt")
    void shouldHandleWhitespaceInPasswordAndSalt() {
        // Given
        String password = "  " + TEST_PASSWORD + "  ";
        String salt = "  " + TEST_SALT + "  ";

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertEquals(salt, request.salt());
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        // Given
        String password = "P@ssw0rd!#$%^&*()";
        String salt = TEST_SALT;

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertEquals(salt, request.salt());
    }

    @Test
    @DisplayName("Should handle hex salt format")
    void shouldHandleHexSaltFormat() {
        // Given
        String password = TEST_PASSWORD;
        String salt = "ABCDEF1234567890FEDCBA0987654321";

        // When
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(password, salt);

        // Then
        assertNotNull(request);
        assertEquals(password, request.password());
        assertEquals(salt, request.salt());
    }
}
