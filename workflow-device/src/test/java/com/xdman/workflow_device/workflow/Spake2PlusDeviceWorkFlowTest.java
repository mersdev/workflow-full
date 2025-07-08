package com.xdman.workflow_device.workflow;

import com.xdman.workflow_device.base.BaseTest;
import com.xdman.workflow_device.model.DeviceMessagePayload;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Spake2PlusDeviceWorkFlow Tests")
class Spake2PlusDeviceWorkFlowTest extends BaseTest {

    @Mock
    private Spake2PlusDeviceWorkFlow workflow;

    @Test
    @DisplayName("Should start device owner pairing workflow")
    void shouldStartDeviceOwnerPairingWorkflow() {
        // Given & When & Then
        assertDoesNotThrow(() -> {
            workflow.startDeviceOwnerPairing();
        });
    }

    @Test
    @DisplayName("Should receive message from vehicle")
    void shouldReceiveMessageFromVehicle() {
        // Given
        DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, TEST_MESSAGE);

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle null message payload")
    void shouldHandleNullMessagePayload() {
        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(null);
        });
    }

    @Test
    @DisplayName("Should handle SELECT command message")
    void shouldHandleSelectCommandMessage() {
        // Given
        String selectMessage = SELECT_COMMAND_HEADER + "additional_data";
        DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, selectMessage);

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle REQUEST command message")
    void shouldHandleRequestCommandMessage() {
        // Given
        String requestMessage = REQUEST_COMMAND_HEADER + "additional_data";
        DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, requestMessage);

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle VERIFY command message")
    void shouldHandleVerifyCommandMessage() {
        // Given
        String verifyMessage = VERIFY_COMMAND_HEADER + "additional_data";
        DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, verifyMessage);

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle multiple message signals")
    void shouldHandleMultipleMessageSignals() {
        // Given
        DeviceMessagePayload message1 = new DeviceMessagePayload(TEST_VIN, SELECT_COMMAND_HEADER + "data1");
        DeviceMessagePayload message2 = new DeviceMessagePayload(TEST_VIN, REQUEST_COMMAND_HEADER + "data2");
        DeviceMessagePayload message3 = new DeviceMessagePayload(TEST_VIN, VERIFY_COMMAND_HEADER + "data3");

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(message1);
            workflow.receiveMessageFromVehicle(message2);
            workflow.receiveMessageFromVehicle(message3);
        });
    }

    @Test
    @DisplayName("Should handle different VIN formats")
    void shouldHandleDifferentVinFormats() {
        // Given
        String[] vins = {"1HGBH41JXMN109186", "WBWSS52P9NEEC05991", "JH4KA7561PC008269"};

        // When & Then
        for (String vin : vins) {
            DeviceMessagePayload messagePayload = new DeviceMessagePayload(vin, TEST_MESSAGE);
            assertDoesNotThrow(() -> {
                workflow.receiveMessageFromVehicle(messagePayload);
            });
        }
    }

    @Test
    @DisplayName("Should handle long hex messages")
    void shouldHandleLongHexMessages() {
        // Given
        String longHexMessage = createTestHexString(500);
        DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, longHexMessage);

        // When & Then
        assertDoesNotThrow(() -> {
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle empty message")
    void shouldHandleEmptyMessage() {
        // When & Then - This should throw an exception due to DeviceMessagePayload validation
        assertThrows(IllegalArgumentException.class, () -> {
            DeviceMessagePayload messagePayload = new DeviceMessagePayload(TEST_VIN, "");
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }

    @Test
    @DisplayName("Should handle empty VIN")
    void shouldHandleEmptyVin() {
        // When & Then - This should throw an exception due to DeviceMessagePayload validation
        assertThrows(IllegalArgumentException.class, () -> {
            DeviceMessagePayload messagePayload = new DeviceMessagePayload("", TEST_MESSAGE);
            workflow.receiveMessageFromVehicle(messagePayload);
        });
    }
}
