package com.xdman.workflow_device.service;

import com.xdman.workflow_device.base.BaseTest;
import com.xdman.workflow_device.model.DeviceMessagePayload;
import com.xdman.workflow_device.workflow.Spake2PlusDeviceWorkFlow;
import com.xdman.workflow_device.workflow.Spake2PlusFullWorkFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@DisplayName("ReceivedFromVehicleService Tests")
class ReceivedFromVehicleServiceTest extends BaseTest {

    @Mock
    private WorkflowClient workflowClient;

    @Mock
    private Spake2PlusFullWorkFlow fullWorkFlow;

    @Mock
    private Spake2PlusDeviceWorkFlow deviceWorkFlow;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @InjectMocks
    private ReceivedFromVehicleService receivedFromVehicleService;

    @BeforeEach
    protected void setUp() {
        super.setUp();
        // Mock setup moved to individual tests where needed to avoid unnecessary stubbing
    }

    @Test
    @DisplayName("Should start full owner pairing cycle successfully")
    void shouldStartFullOwnerPairingCycleSuccessfully() {
        // Given
        String expectedResult = "Full cycle completed successfully";
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(eq(Spake2PlusFullWorkFlow.class), any(WorkflowOptions.class)))
                .thenReturn(fullWorkFlow);
            when(fullWorkFlow.processFullCycleOwnerPairing(TEST_REQUEST_ID, TEST_PASSWORD, TEST_SALT))
                .thenReturn(expectedResult);

            // When
            String result = receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

            // Then
            assertEquals(expectedResult, result);
            verify(workflowClient).newWorkflowStub(eq(Spake2PlusFullWorkFlow.class), any(WorkflowOptions.class));
            verify(fullWorkFlow).processFullCycleOwnerPairing(TEST_REQUEST_ID, TEST_PASSWORD, TEST_SALT);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is null")
    void shouldThrowExceptionWhenPasswordIsNull() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, null, TEST_SALT)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, deviceWorkFlow);
    }

    @Test
    @DisplayName("Should throw exception when salt is null")
    void shouldThrowExceptionWhenSaltIsNull() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, null)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, deviceWorkFlow);
    }

    @Test
    @DisplayName("Should start device owner pairing when SELECT command is detected")
    void shouldStartDeviceOwnerPairingWhenSelectCommandDetected() throws Exception {
        // Given
        String messageWithSelectCommand = SELECT_COMMAND_HEADER + "additional_data";
        String expectedResult = "SPAKE2+ for vehicle " + TEST_VIN + " started successfully!";
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class);
             MockedStatic<WorkflowClient> mockedWorkflowClient = mockStatic(WorkflowClient.class)) {
            
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(eq(Spake2PlusDeviceWorkFlow.class), any(WorkflowOptions.class)))
                .thenReturn(deviceWorkFlow);

            // When
            String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, messageWithSelectCommand);

            // Then
            assertEquals(expectedResult, result);
            verify(deviceWorkFlow).receiveMessageFromVehicle(any(DeviceMessagePayload.class));
        }
    }

    @Test
    @DisplayName("Should signal workflow when REQUEST command is detected")
    void shouldSignalWorkflowWhenRequestCommandDetected() throws Exception {
        // Given
        String messageWithRequestCommand = REQUEST_COMMAND_HEADER + "additional_data";
        String expectedResult = "Received a message from vehicle successfully " + messageWithRequestCommand;
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(Spake2PlusDeviceWorkFlow.class, TEST_REQUEST_ID))
                .thenReturn(deviceWorkFlow);

            // When
            String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, messageWithRequestCommand);

            // Then
            assertEquals(expectedResult, result);
            verify(workflowClient).newWorkflowStub(Spake2PlusDeviceWorkFlow.class, TEST_REQUEST_ID);
            verify(deviceWorkFlow).receiveMessageFromVehicle(any(DeviceMessagePayload.class));
        }
    }

    @Test
    @DisplayName("Should signal workflow when VERIFY command is detected")
    void shouldSignalWorkflowWhenVerifyCommandDetected() throws Exception {
        // Given
        String messageWithVerifyCommand = VERIFY_COMMAND_HEADER + "additional_data";
        String expectedResult = "Received a message from vehicle successfully " + messageWithVerifyCommand;
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(Spake2PlusDeviceWorkFlow.class, TEST_REQUEST_ID))
                .thenReturn(deviceWorkFlow);

            // When
            String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, messageWithVerifyCommand);

            // Then
            assertEquals(expectedResult, result);
            verify(workflowClient).newWorkflowStub(Spake2PlusDeviceWorkFlow.class, TEST_REQUEST_ID);
            verify(deviceWorkFlow).receiveMessageFromVehicle(any(DeviceMessagePayload.class));
        }
    }

    @Test
    @DisplayName("Should throw exception when message is null")
    void shouldThrowExceptionWhenMessageIsNull() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, null)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, deviceWorkFlow);
    }

    @Test
    @DisplayName("Should handle unknown message format")
    void shouldHandleUnknownMessageFormat() throws Exception {
        // Given
        String unknownMessage = "UNKNOWN_COMMAND_FORMAT";

        // When & Then - Just verify the method can be called without throwing
        // No mocks needed since unknown message format returns a message without any service calls
        String result = receivedFromVehicleService.receiveMessageFromVehicle(TEST_VIN, unknownMessage);
        assertEquals("Message received but no recognized command header found", result);

        // Verify no workflow interactions since message format is unknown
        verifyNoInteractions(workflowClient, deviceWorkFlow, fullWorkFlow);
    }

    @Test
    @DisplayName("Should handle workflow client exceptions")
    void shouldHandleWorkflowClientExceptions() {
        // Given
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(eq(Spake2PlusFullWorkFlow.class), any(WorkflowOptions.class)))
                .thenThrow(new RuntimeException("Workflow client error"));

            // When & Then
            assertThrows(
                RuntimeException.class,
                () -> receivedFromVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT)
            );
        }
    }
}
