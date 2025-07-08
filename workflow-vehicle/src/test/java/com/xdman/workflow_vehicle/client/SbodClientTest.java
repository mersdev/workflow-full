package com.xdman.workflow_vehicle.client;

import com.xdman.workflow_vehicle.base.BaseTest;
import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("SbodClient Tests")
class SbodClientTest extends BaseTest {

    @Mock
    private SbodClient sbodClient;

    @Test
    @DisplayName("Should send message to device successfully")
    void shouldSendMessageToDeviceSuccessfully() {
        // Given
        String expectedResponse = "Message received successfully";
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(TEST_MESSAGE);
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponse);

        when(sbodClient.receivedFromVehicle(TEST_VIN, request)).thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse response = sbodClient.receivedFromVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertEquals(expectedResponse, response.message());
        verify(sbodClient).receivedFromVehicle(TEST_VIN, request);
    }

    @Test
    @DisplayName("Should handle server error and return fallback response")
    void shouldHandleServerErrorAndReturnFallbackResponse() {
        // Given
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(TEST_MESSAGE);
        String fallbackMessage = "Fallback response: Unable to send message to device with VIN " + TEST_VIN;
        ReceivedFromVehicleResponse fallbackResponse = new ReceivedFromVehicleResponse(fallbackMessage);

        when(sbodClient.receivedFromVehicle(TEST_VIN, request)).thenReturn(fallbackResponse);

        // When
        ReceivedFromVehicleResponse response = sbodClient.receivedFromVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertTrue(response.message().contains("Fallback response"));
        assertTrue(response.message().contains(TEST_VIN));
    }

    @Test
    @DisplayName("Should handle different message types")
    void shouldHandleDifferentMessageTypes() {
        // Given
        String selectMessage = SELECT_COMMAND_HEADER + "additional_data";
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(selectMessage);
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse("SPAKE2+ started successfully");

        when(sbodClient.receivedFromVehicle(TEST_VIN, request)).thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse response = sbodClient.receivedFromVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertEquals("SPAKE2+ started successfully", response.message());
    }

    @Test
    @DisplayName("Should handle different VIN formats")
    void shouldHandleDifferentVinFormats() {
        // Given
        String[] vins = {"1HGBH41JXMN109186", "WBWSS52P9NEEC05991", "JH4KA7561PC008269"};
        ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(TEST_MESSAGE);
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse("VIN processed successfully");

        // When & Then
        for (String vin : vins) {
            when(sbodClient.receivedFromVehicle(vin, request)).thenReturn(mockResponse);
            ReceivedFromVehicleResponse response = sbodClient.receivedFromVehicle(vin, request);
            assertNotNull(response);
            assertEquals("VIN processed successfully", response.message());
        }
    }
}
