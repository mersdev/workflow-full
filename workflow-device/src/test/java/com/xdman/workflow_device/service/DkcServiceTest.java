package com.xdman.workflow_device.service;

import com.xdman.workflow_device.base.BaseTest;
import com.xdman.workflow_device.client.DkcClient;
import com.xdman.workflow_device.model.request.SendToVehicleRequest;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("DkcService Tests")
class DkcServiceTest extends BaseTest {

    @Mock
    private DkcClient dkcClient;

    @InjectMocks
    private DkcService dkcService;

    @Test
    @DisplayName("Should publish command message to DKC successfully")
    void shouldPublishCommandMessageToDkcSuccessfully() throws Exception {
        // Given
        String commandMessage = TEST_MESSAGE;
        String expectedResponse = "Message sent successfully";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class));
    }

    @Test
    @DisplayName("Should handle DKC client exceptions")
    void shouldHandleDkcClientExceptions() {
        // Given
        String commandMessage = TEST_MESSAGE;
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenThrow(new RuntimeException("DKC client error"));

        // When & Then
        assertThrows(
            Exception.class,
            () -> dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage)
        );
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class));
    }

    @Test
    @DisplayName("Should create correct SendToVehicleRequest")
    void shouldCreateCorrectSendToVehicleRequest() throws Exception {
        // Given
        String commandMessage = TEST_MESSAGE;
        String expectedResponse = "Success";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage);

        // Then
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals(commandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle null command message")
    void shouldHandleNullCommandMessage() throws Exception {
        // Given
        String commandMessage = null;
        String expectedResponse = "Success";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload() == null
        ));
    }

    @Test
    @DisplayName("Should handle empty command message")
    void shouldHandleEmptyCommandMessage() throws Exception {
        // Given
        String commandMessage = "";
        String expectedResponse = "Success";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals("")
        ));
    }

    @Test
    @DisplayName("Should handle hex command message")
    void shouldHandleHexCommandMessage() throws Exception {
        // Given
        String hexCommandMessage = createTestHexString(100);
        String expectedResponse = "Hex message sent successfully";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, hexCommandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals(hexCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle SELECT command message")
    void shouldHandleSelectCommandMessage() throws Exception {
        // Given
        String selectCommandMessage = SELECT_COMMAND_HEADER + "additional_data";
        String expectedResponse = "Select command sent successfully";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, selectCommandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals(selectCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle REQUEST command message")
    void shouldHandleRequestCommandMessage() throws Exception {
        // Given
        String requestCommandMessage = REQUEST_COMMAND_HEADER + "additional_data";
        String expectedResponse = "Request command sent successfully";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, requestCommandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals(requestCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle VERIFY command message")
    void shouldHandleVerifyCommandMessage() throws Exception {
        // Given
        String verifyCommandMessage = VERIFY_COMMAND_HEADER + "additional_data";
        String expectedResponse = "Verify command sent successfully";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, verifyCommandMessage);

        // Then
        assertEquals(expectedResponse, result);
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), argThat(request -> 
            request.messagePayload().equals(verifyCommandMessage)
        ));
    }

    @Test
    @DisplayName("Should handle fallback response")
    void shouldHandleFallbackResponse() throws Exception {
        // Given
        String commandMessage = TEST_MESSAGE;
        String fallbackMessage = "Fallback response: Unable to send message to vehicle with VIN " + TEST_VIN;
        SendToVehicleResponse fallbackResponse = new SendToVehicleResponse(fallbackMessage);
        
        when(dkcClient.sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class)))
            .thenReturn(fallbackResponse);

        // When
        String result = dkcService.publishCommandMessageToDkc(TEST_VIN, commandMessage);

        // Then
        assertEquals(fallbackMessage, result);
        assertTrue(result.contains("Fallback response"));
        verify(dkcClient).sendMessageToVehicle(eq(TEST_VIN), any(SendToVehicleRequest.class));
    }

    @Test
    @DisplayName("Should handle different VIN formats")
    void shouldHandleDifferentVinFormats() throws Exception {
        // Given
        String[] vins = {"1HGBH41JXMN109186", "WBWSS52P9NEEC05991", "JH4KA7561PC008269"};
        String commandMessage = TEST_MESSAGE;
        String expectedResponse = "Success";
        SendToVehicleResponse mockResponse = new SendToVehicleResponse(expectedResponse);
        
        when(dkcClient.sendMessageToVehicle(anyString(), any(SendToVehicleRequest.class)))
            .thenReturn(mockResponse);

        // When & Then
        for (String vin : vins) {
            String result = dkcService.publishCommandMessageToDkc(vin, commandMessage);
            assertEquals(expectedResponse, result);
            verify(dkcClient).sendMessageToVehicle(eq(vin), any(SendToVehicleRequest.class));
        }
    }
}
