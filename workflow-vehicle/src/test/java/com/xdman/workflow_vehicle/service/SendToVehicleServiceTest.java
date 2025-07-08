package com.xdman.workflow_vehicle.service;

import com.xdman.workflow_vehicle.base.BaseTest;
import com.xdman.workflow_vehicle.workflow.Spake2PlusFullWorkFlow;
import com.xdman.workflow_vehicle.workflow.Spake2PlusVehicleWorkFlow;
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

@DisplayName("SendToVehicleService Tests")
class SendToVehicleServiceTest extends BaseTest {

    @Mock
    private WorkflowClient workflowClient;

    @Mock
    private Spake2PlusFullWorkFlow fullWorkFlow;

    @Mock
    private Spake2PlusVehicleWorkFlow vehicleWorkFlow;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private ServletRequestAttributes servletRequestAttributes;

    @InjectMocks
    private SendToVehicleService sendToVehicleService;

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
            String result = sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

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
            () -> sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, null, TEST_SALT)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
    }

    @Test
    @DisplayName("Should throw exception when salt is null")
    void shouldThrowExceptionWhenSaltIsNull() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, null)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
    }

    @Test
    @DisplayName("Should send to vehicle successfully")
    void shouldSendToVehicleSuccessfully() {
        // Given
        String message = TEST_MESSAGE;
        String expectedResult = "Message signaled to workflow successfully";
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(Spake2PlusVehicleWorkFlow.class, TEST_REQUEST_ID))
                .thenReturn(vehicleWorkFlow);

            // When
            String result = sendToVehicleService.sendToVehicle(TEST_VIN, message);

            // Then
            assertNotNull(result);
            verify(workflowClient).newWorkflowStub(Spake2PlusVehicleWorkFlow.class, TEST_REQUEST_ID);
        }
    }

    @Test
    @DisplayName("Should throw exception when message is null in sendToVehicle")
    void shouldThrowExceptionWhenMessageIsNullInSendToVehicle() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> sendToVehicleService.sendToVehicle(TEST_VIN, null)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
    }

    @Test
    @DisplayName("Should start owner pairing cycle successfully")
    void shouldStartOwnerPairingCycleSuccessfully() {
        // Given
        String expectedResult = "Vehicle workflow started successfully";
        
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(TEST_REQUEST_ID);

            when(workflowClient.newWorkflowStub(eq(Spake2PlusVehicleWorkFlow.class), any(WorkflowOptions.class)))
                .thenReturn(vehicleWorkFlow);
            when(vehicleWorkFlow.startVehicleWorkflow(TEST_VIN, TEST_PASSWORD, TEST_SALT))
                .thenReturn(expectedResult);

            // When
            String result = sendToVehicleService.startOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

            // Then
            assertEquals(expectedResult, result);
            verify(workflowClient).newWorkflowStub(eq(Spake2PlusVehicleWorkFlow.class), any(WorkflowOptions.class));
            verify(vehicleWorkFlow).startVehicleWorkflow(TEST_VIN, TEST_PASSWORD, TEST_SALT);
        }
    }

    @Test
    @DisplayName("Should throw exception when password is null in startOwnerPairingCycle")
    void shouldThrowExceptionWhenPasswordIsNullInStartOwnerPairingCycle() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> sendToVehicleService.startOwnerPairingCycle(TEST_VIN, null, TEST_SALT)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
    }

    @Test
    @DisplayName("Should throw exception when salt is null in startOwnerPairingCycle")
    void shouldThrowExceptionWhenSaltIsNullInStartOwnerPairingCycle() {
        // When & Then
        assertThrows(
            IllegalArgumentException.class,
            () -> sendToVehicleService.startOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, null)
        );

        // Verify no interactions with mocks since exception is thrown early
        verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
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
                () -> sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT)
            );
        }
    }

    @Test
    @DisplayName("Should handle missing request context")
    void shouldHandleMissingRequestContext() {
        // Given
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(null);

            // When & Then
            assertThrows(
                NullPointerException.class,
                () -> sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT)
            );

            // Verify no interactions with workflow mocks since exception is thrown early
            verifyNoInteractions(workflowClient, fullWorkFlow, vehicleWorkFlow);
        }
    }

    @Test
    @DisplayName("Should handle missing request ID header")
    void shouldHandleMissingRequestIdHeader() {
        // Given
        try (MockedStatic<RequestContextHolder> mockedRequestContextHolder = mockStatic(RequestContextHolder.class)) {
            mockedRequestContextHolder.when(RequestContextHolder::getRequestAttributes)
                .thenReturn(servletRequestAttributes);
            when(servletRequestAttributes.getRequest()).thenReturn(httpServletRequest);
            when(httpServletRequest.getHeader("x-requestId")).thenReturn(null);

            when(workflowClient.newWorkflowStub(eq(Spake2PlusFullWorkFlow.class), any(WorkflowOptions.class)))
                .thenReturn(fullWorkFlow);
            when(fullWorkFlow.processFullCycleOwnerPairing(null, TEST_PASSWORD, TEST_SALT))
                .thenReturn("Success with null request ID");

            // When
            String result = sendToVehicleService.startFullOwnerPairingCycle(TEST_VIN, TEST_PASSWORD, TEST_SALT);

            // Then
            assertEquals("Success with null request ID", result);
            verify(fullWorkFlow).processFullCycleOwnerPairing(null, TEST_PASSWORD, TEST_SALT);
        }
    }
}
