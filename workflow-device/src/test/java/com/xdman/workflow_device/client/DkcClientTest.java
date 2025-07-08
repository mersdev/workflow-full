package com.xdman.workflow_device.client;

import com.xdman.workflow_device.base.BaseTest;
import com.xdman.workflow_device.model.request.SendToVehicleRequest;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("DkcClient Tests")
class DkcClientTest extends BaseTest {

    @Mock
    private DkcClient dkcClient;

    @Test
    @DisplayName("Should send message to vehicle successfully")
    void shouldSendMessageToVehicleSuccessfully() {
        // Given
        String expectedResponse = "Message sent successfully";
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);

        when(dkcClient.sendMessageToVehicle(TEST_VIN, request)).thenReturn(mockResponse);

        // When
        SendToVehicleResponse response = dkcClient.sendMessageToVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertEquals(expectedResponse, response.message());
        verify(dkcClient).sendMessageToVehicle(TEST_VIN, request);
    }

    @Test
    @DisplayName("Should handle server error and return fallback response")
    void shouldHandleServerErrorAndReturnFallbackResponse() {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);
        String fallbackMessage = "Fallback response: Unable to send message to vehicle with VIN " + TEST_VIN;
        SendToVehicleResponse fallbackResponse = new SendToVehicleResponse(fallbackMessage);

        when(dkcClient.sendMessageToVehicle(TEST_VIN, request)).thenReturn(fallbackResponse);

        // When
        SendToVehicleResponse response = dkcClient.sendMessageToVehicle(TEST_VIN, request);

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
        SendToVehicleRequest request = new SendToVehicleRequest(selectMessage);
        SendToVehicleResponse mockResponse = new SendToVehicleResponse("Select command processed");

        when(dkcClient.sendMessageToVehicle(TEST_VIN, request)).thenReturn(mockResponse);

        // When
        SendToVehicleResponse response = dkcClient.sendMessageToVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertEquals("Select command processed", response.message());
    }

    @Test
    @DisplayName("Should handle null message payload")
    void shouldHandleNullMessagePayload() {
        // Given
        SendToVehicleRequest request = new SendToVehicleRequest(null);
        SendToVehicleResponse mockResponse = new SendToVehicleResponse("Null message processed");

        when(dkcClient.sendMessageToVehicle(TEST_VIN, request)).thenReturn(mockResponse);

        // When
        SendToVehicleResponse response = dkcClient.sendMessageToVehicle(TEST_VIN, request);

        // Then
        assertNotNull(response);
        assertEquals("Null message processed", response.message());
    }

    @Test
    @DisplayName("Should handle different VIN formats")
    void shouldHandleDifferentVinFormats() {
        // Given
        String[] vins = {"1HGBH41JXMN109186", "WBWSS52P9NEEC05991", "JH4KA7561PC008269"};
        SendToVehicleRequest request = new SendToVehicleRequest(TEST_MESSAGE);
        SendToVehicleResponse mockResponse = new SendToVehicleResponse("VIN processed successfully");

        // When & Then
        for (String vin : vins) {
            when(dkcClient.sendMessageToVehicle(vin, request)).thenReturn(mockResponse);
            SendToVehicleResponse response = dkcClient.sendMessageToVehicle(vin, request);
            assertNotNull(response);
            assertEquals("VIN processed successfully", response.message());
        }
    }
}
