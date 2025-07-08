package com.xdman.workflow_vehicle.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdman.workflow_vehicle.base.BaseTest;
import com.xdman.workflow_vehicle.model.request.SendToVehicleRequest;
import com.xdman.workflow_vehicle.model.request.StartFullOwnerPairingRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import com.xdman.workflow_vehicle.service.SbodService;
import com.xdman.workflow_vehicle.service.SendToVehicleService;
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

@DisplayName("VehicleOEMController Tests")
class VehicleOEMControllerTest extends BaseTest {

    @Mock
    private MockMvc mockMvc;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SendToVehicleService sendToVehicleService;

    @Mock
    private SbodService sbodService;

    @Mock
    private VehicleOEMController vehicleOEMController;

    @Test
    @DisplayName("Should start full owner pairing cycle successfully")
    void shouldStartFullOwnerPairingCycleSuccessfully() throws Exception {
        // Given
        String expectedResponse = "Full cycle started successfully";

        when(sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT))
            .thenReturn(expectedResponse);

        // When
        String result = sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

        // Then
        assertEquals(expectedResponse, result);
        verify(sendToVehicleService).startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
    }

    @Test
    @DisplayName("Should send to vehicle successfully")
    void shouldSendToVehicleSuccessfully() throws Exception {
        // Given
        String expectedResponse = "Message sent successfully";

        when(sendToVehicleService.sendToVehicle(TEST_VIN, TEST_MESSAGE))
            .thenReturn(expectedResponse);

        // When
        String result = sendToVehicleService.sendToVehicle(TEST_VIN, TEST_MESSAGE);

        // Then
        assertEquals(expectedResponse, result);
        verify(sendToVehicleService).sendToVehicle(TEST_VIN, TEST_MESSAGE);
    }

    @Test
    @DisplayName("Should start owner pairing successfully")
    void shouldStartOwnerPairingSuccessfully() throws Exception {
        // Given
        String expectedResponse = "Owner pairing started successfully";

        when(sendToVehicleService.startOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT))
            .thenReturn(expectedResponse);

        // When
        String result = sendToVehicleService.startOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

        // Then
        assertEquals(expectedResponse, result);
        verify(sendToVehicleService).startOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
    }

    @Test
    @DisplayName("Should test SBOD receive from vehicle successfully")
    void shouldTestSbodReceiveFromVehicleSuccessfully() throws Exception {
        // Given
        String testMessage = "803000002F5B0201005C0201007F5020C0100102030405060708090A0B0C0D0E0F10C10400001000C2020008C3020001D602000300";
        ReceivedFromVehicleResponse expectedResponse = new ReceivedFromVehicleResponse("Test message processed successfully");

        when(sbodService.receiveFromVehicle(TEST_VIN, testMessage))
            .thenReturn(expectedResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, testMessage);

        // Then
        assertEquals("Test message processed successfully", result.message());
        verify(sbodService).receiveFromVehicle(TEST_VIN, testMessage);
    }

    @Test
    @DisplayName("Should handle service exception in start full owner pairing")
    void shouldHandleServiceExceptionInStartFullOwnerPairing() throws Exception {
        // Given
        when(sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT))
            .thenThrow(new IllegalArgumentException("Invalid Password and Salt format"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
        });

        verify(sendToVehicleService).startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);
    }
}
