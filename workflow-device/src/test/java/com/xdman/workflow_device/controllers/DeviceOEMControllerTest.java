package com.xdman.workflow_device.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdman.workflow_device.base.BaseTest;
import com.xdman.workflow_device.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_device.model.request.StartFullOwnerPairingRequest;
import com.xdman.workflow_device.service.ReceivedFromVehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("DeviceOEMController Tests")
class DeviceOEMControllerTest extends BaseTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private ReceivedFromVehicleService receivedFromVehicleService;

    @Mock
    private DeviceOEMController deviceOEMController;

    @Test
    @DisplayName("Should start full owner pairing cycle successfully")
    void shouldStartFullOwnerPairingCycleSuccessfully() throws Exception {
        // Given
        StartFullOwnerPairingRequest request = new StartFullOwnerPairingRequest(TEST_PASSWORD, TEST_SALT);
        String expectedResponse = "Full cycle started successfully";

        when(receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT))
            .thenReturn(expectedResponse);

        // When
        String result = receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

        // Then
        assertEquals(expectedResponse, result);
        verify(receivedFromVehicleService).startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
    }

    @Test
    @DisplayName("Should handle service exception in start full owner pairing")
    void shouldHandleServiceExceptionInStartFullOwnerPairing() throws Exception {
        // Given
        when(receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT))
            .thenThrow(new IllegalArgumentException("Invalid Password and Salt format"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
        });

        verify(receivedFromVehicleService).startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
    }

    @Test
    @DisplayName("Should receive message from vehicle successfully")
    void shouldReceiveMessageFromVehicleSuccessfully() throws Exception {
        // Given
        String expectedResponse = "Message received successfully";

        when(receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE))
            .thenReturn(expectedResponse);

        // When
        String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE);

        // Then
        assertEquals(expectedResponse, result);
        verify(receivedFromVehicleService).receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE);
    }

    @Test
    @DisplayName("Should handle service exception in receive from vehicle")
    void shouldHandleServiceExceptionInReceiveFromVehicle() throws Exception {
        // Given
        when(receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE))
            .thenThrow(new IllegalArgumentException("Invalid message format"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE);
        });

        verify(receivedFromVehicleService).receiveMessageFromVehicle(TEST_VIN, TEST_MESSAGE);
    }

    @Test
    @DisplayName("Should handle SELECT command message")
    void shouldHandleSelectCommandMessage() throws Exception {
        // Given
        String selectMessage = SELECT_COMMAND_HEADER + "additional_data";
        String expectedResponse = "SPAKE2+ for vehicle " + TEST_VIN + " started successfully!";

        when(receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, selectMessage))
            .thenReturn(expectedResponse);

        // When
        String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, selectMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(receivedFromVehicleService).receiveMessageFromVehicle(TEST_VIN, selectMessage);
    }
}
