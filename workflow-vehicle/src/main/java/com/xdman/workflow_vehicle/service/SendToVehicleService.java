package com.xdman.workflow_vehicle.service;

import com.xdman.workflow_vehicle.workflow.Spake2PlusFullWorkFlow;
import com.xdman.workflow_vehicle.workflow.Spake2PlusVehicleWorkFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class SendToVehicleService {
  private static final String REQUEST_COMMAND_HEADER = "803000";
  private static final String VERIFY_COMMAND_HEADER = "803200";

  @Autowired
  private WorkflowClient workflowClient;

  public String startFullOwnerPairingCycle(String vin, String password, String salt) {
    if (password == null || salt == null) {
      throw new IllegalArgumentException("Invalid Password and Salt format");
    }
    log.info("Password: {}, salt: {}", password, salt);

    String requestId = getRequestIdFromContext();
    log.info("requestId: {}", requestId);

    Spake2PlusFullWorkFlow workFlow = workflowClient.newWorkflowStub(
      Spake2PlusFullWorkFlow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue("Spake2PlusTaskQueue")
        .setWorkflowId(requestId)
        .build());
    return workFlow.processFullCycleOwnerPairing(requestId, password, salt);
  }

  public String sendToVehicle(String vin, String message){
    if (message == null) {
      throw new IllegalArgumentException("Invalid message format");
    }

    log.info("Received message from vehicle VIN: {}, Message: {}", vin, message);
    log.info("Verify command detected, signaling workflow");
    String requestId = getRequestIdFromContext();
    log.info("requestId for signaling: {}", requestId);
    return signalWorkFlow(requestId, message);
  }

  public String startOwnerPairingCycle(String vin, String password, String salt) {
    if (password == null || salt == null) {
      throw new IllegalArgumentException("Invalid Password and Salt format");
    }

    String requestId = getRequestIdFromContext();

    Spake2PlusVehicleWorkFlow workFlow = workflowClient.newWorkflowStub(
      Spake2PlusVehicleWorkFlow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue("Spake2PlusTaskQueue")
        .setWorkflowId(requestId)
        .build());
    return workFlow.startVehicleWorkflow(vin, password, salt, requestId);
  }

  private String signalWorkFlow(String requestId, String tlvMessage) {
    try {
      log.info("Attempting to signal workflow with requestId: {}", requestId);
      Spake2PlusVehicleWorkFlow workFlow = workflowClient.newWorkflowStub(Spake2PlusVehicleWorkFlow.class, requestId);
      workFlow.receiveMessageFromVehicle(tlvMessage);
      log.info("Successfully signaled workflow with requestId: {}", requestId);
      return "Received a message from vehicle successfully " + tlvMessage;
    } catch (Exception e) {
      log.warn("Workflow with requestId {} does not exist, this is expected for device-initiated flows", requestId);
      log.info("Message received but no corresponding vehicle workflow found. This is normal for device-initiated communication.");
      // Return success message since this is expected behavior when device initiates communication
      // without a corresponding vehicle workflow
      return "Message received successfully (no vehicle workflow correlation needed): " + tlvMessage;
    }
  }

  /**
   * Safely extracts x-requestId from the current request context.
   * If no x-requestId is found or request context is not available, generates a new UUID.
   */
  private String getRequestIdFromContext() {
    try {
      HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
      String requestId = requestHeader.getHeader("x-requestId");
      if (requestId != null && !requestId.trim().isEmpty()) {
        return requestId;
      }
    } catch (Exception e) {
      log.warn("Could not extract x-requestId from request context: {}", e.getMessage());
    }

    // Generate a new UUID if no x-requestId found
    String generatedId = UUID.randomUUID().toString();
    log.info("Generated new requestId: {}", generatedId);
    return generatedId;
  }
}