package com.xdman.workflow_vehicle.base;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

/**
 * Base test class providing common test setup and utilities
 */
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public abstract class BaseTest {

    @BeforeEach
    protected void setUp() {
        // Common setup for all tests
    }

    /**
     * Common test data and utilities
     */
    protected static final String TEST_VIN = "1HGBH41JXMN109186";
    protected static final String TEST_PASSWORD = "testPassword123";
    protected static final String TEST_SALT = "0102030405060708090A0B0C0D0E0F10";
    protected static final String TEST_REQUEST_ID = "test-request-id-123";
    protected static final String TEST_MESSAGE = "803000002F5B0201005C0201007F5020C0100102030405060708090A0B0C0D0E0F10C10400001000C2020008C3020001D602000300";
    protected static final String SELECT_COMMAND_HEADER = "00A40400";
    protected static final String REQUEST_COMMAND_HEADER = "803000";
    protected static final String VERIFY_COMMAND_HEADER = "803200";

    /**
     * Helper method to create test hex string
     */
    protected String createTestHexString(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(String.format("%02X", i % 256));
        }
        return sb.toString();
    }

    /**
     * Helper method to create test byte array
     */
    protected byte[] createTestByteArray(int length) {
        byte[] array = new byte[length];
        for (int i = 0; i < length; i++) {
            array[i] = (byte) (i % 256);
        }
        return array;
    }
}
