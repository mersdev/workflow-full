package com.xdman.workflow_vehicle.service;

import com.xdman.workflow_vehicle.base.BaseTest;
import com.xdman.workflow_vehicle.client.SbodClient;
import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("SbodService Tests")
class SbodServiceTest extends BaseTest {

    @Mock
    private SbodClient sbodClient;

    @InjectMocks
    private SbodService sbodService;

    @Test
    @DisplayName("Should receive from vehicle successfully")
    void shouldReceiveFromVehicleSuccessfully() {
        // Given
        String messagePayload = TEST_MESSAGE;
        String expectedResponseMessage = "Message received successfully";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, messagePayload);

        // Then
        assertNotNull(result);
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class));
    }

    @Test
    @DisplayName("Should handle SBOD client exceptions")
    void shouldHandleSbodClientExceptions() {
        // Given
        String messagePayload = TEST_MESSAGE;
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenThrow(new RuntimeException("SBOD client error"));

        // When & Then
        assertThrows(
            RuntimeException.class,
            () -> sbodService.receiveFromVehicle(TEST_VIN, messagePayload)
        );
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class));
    }

    @Test
    @DisplayName("Should create correct ReceivedFromVehicleRequest")
    void shouldCreateCorrectReceivedFromVehicleRequest() {
        // Given
        String messagePayload = TEST_MESSAGE;
        String expectedResponseMessage = "Success";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        sbodService.receiveFromVehicle(TEST_VIN, messagePayload);

        // Then
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals(messagePayload)
        ));
    }

    @Test
    @DisplayName("Should handle null message payload")
    void shouldHandleNullMessagePayload() {
        // Given
        String messagePayload = null;
        String expectedResponseMessage = "Success";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, messagePayload);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message() == null
        ));
    }

    @Test
    @DisplayName("Should handle empty message payload")
    void shouldHandleEmptyMessagePayload() {
        // Given
        String messagePayload = "";
        String expectedResponseMessage = "Success";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, messagePayload);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals("")
        ));
    }

    @Test
    @DisplayName("Should handle hex message payload")
    void shouldHandleHexMessagePayload() {
        // Given
        String hexMessagePayload = createTestHexString(100);
        String expectedResponseMessage = "Hex message processed successfully";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, hexMessagePayload);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals(hexMessagePayload)
        ));
    }

    @Test
    @DisplayName("Should handle SELECT command message")
    void shouldHandleSelectCommandMessage() {
        // Given
        String selectCommandMessage = SELECT_COMMAND_HEADER + "additional_data";
        String expectedResponseMessage = "SPAKE2+ for vehicle " + TEST_VIN + " started successfully!";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, selectCommandMessage);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals(selectCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle REQUEST command message")
    void shouldHandleRequestCommandMessage() {
        // Given
        String requestCommandMessage = REQUEST_COMMAND_HEADER + "additional_data";
        String expectedResponseMessage = "Received a message from vehicle successfully " + requestCommandMessage;
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, requestCommandMessage);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals(requestCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle VERIFY command message")
    void shouldHandleVerifyCommandMessage() {
        // Given
        String verifyCommandMessage = VERIFY_COMMAND_HEADER + "additional_data";
        String expectedResponseMessage = "Received a message from vehicle successfully " + verifyCommandMessage;
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, verifyCommandMessage);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), argThat(request -> 
            request.message().equals(verifyCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle fallback response")
    void shouldHandleFallbackResponse() {
        // Given
        String messagePayload = TEST_MESSAGE;
        String fallbackMessage = "Fallback response: Unable to send message to device with VIN " + TEST_VIN;
        ReceivedFromVehicleResponse fallbackResponse = new ReceivedFromVehicleResponse(fallbackMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(fallbackResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, messagePayload);

        // Then
        assertEquals(fallbackMessage, result.message());
        assertTrue(result.message().contains("Fallback response"));
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class));
    }

    @Test
    @DisplayName("Should handle different VIN formats")
    void shouldHandleDifferentVinFormats() {
        // Given
        String[] vins = {"1HGBH41JXMN109186", "WBWSS52P9NEEC05991", "JH4KA7561PC008269"};
        String messagePayload = TEST_MESSAGE;
        String expectedResponseMessage = "Success";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(anyString(), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        for (String vin : vins) {
            ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(vin, messagePayload);
            assertEquals(expectedResponseMessage, result.message());
            verify(sbodClient).receivedFromVehicle(eq(vin), any(ReceivedFromVehicleRequest.class));
        }
    }

    @Test
    @DisplayName("Should handle long message payload")
    void shouldHandleLongMessagePayload() {
        // Given
        String longMessagePayload = createTestHexString(1000);
        String expectedResponseMessage = "Long message processed successfully";
        ReceivedFromVehicleResponse mockResponse = new ReceivedFromVehicleResponse(expectedResponseMessage);
        
        when(sbodClient.receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        ReceivedFromVehicleResponse result = sbodService.receiveFromVehicle(TEST_VIN, longMessagePayload);

        // Then
        assertEquals(expectedResponseMessage, result.message());
        verify(sbodClient).receivedFromVehicle(eq(TEST_VIN), any(ReceivedFromVehicleRequest.class));
    }
}
