package com.xdman.workflow_device.service;

import com.xdman.workflow_device.model.DeviceMessagePayload;
import com.xdman.workflow_device.workflow.Spake2PlusDeviceWorkFlow;
import com.xdman.workflow_device.workflow.Spake2PlusFullWorkFlow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Service
@Slf4j
public class ReceivedFromVehicleService {
  private static final String SELECT_COMMAND_HEADER = "00A40400";
  private static final String REQUEST_COMMAND_HEADER = "803000";
  private static final String VERIFY_COMMAND_HEADER = "803200";

  @Autowired
  private WorkflowClient workflowClient;

  public String startFullOwnerPairingCycle(String vin, String password, String salt) {
    if (password == null || salt == null) {
      throw new IllegalArgumentException("Invalid Password and Salt format");
    }
    log.info("Password: {}, salt: {}", password, salt);

    HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    String requestId = requestHeader.getHeader("x-requestId");
    log.info("requestId: {}", requestId);

    Spake2PlusFullWorkFlow workFlow = workflowClient.newWorkflowStub(
      Spake2PlusFullWorkFlow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue("Spake2PlusTaskQueue")
        .setWorkflowId(requestId)
        .build());
    return workFlow.processFullCycleOwnerPairing(requestId, password, salt);
  }

  public String receiveMessageFromVehicle(String vin, String message) throws Exception {
    if (message == null) {
      throw new IllegalArgumentException("Invalid message format");
    }

    log.info("Received message from vehicle VIN: {}, Message: {}", vin, message);

    // Check if message contains REQUEST_COMMAND_HEADER and start device owner pairing
    if (message.contains(SELECT_COMMAND_HEADER)) {
      log.info("Select command detected, starting device owner pairing");
      return startDeviceOwnerPairing(vin, message);
    }

    // Check if message contains VERIFY_COMMAND_HEADER and signal workflow
    if (message.contains(VERIFY_COMMAND_HEADER) || message.contains(REQUEST_COMMAND_HEADER)) {
      log.info("Request or Verify command detected, signaling workflow");
      HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
      String requestId = requestHeader.getHeader("x-requestId");
      log.info("requestId for signaling: {}", requestId);
      return signalWorkFlow(requestId, vin, message);
    }

    // If message doesn't contain either header, log and return appropriate response
    log.warn("Received message does not contain recognized command headers. Message: {}", message);
    return "Message received but no recognized command header found";
  }

  @SneakyThrows
  private String startDeviceOwnerPairing(String vin, String message){
    if (message == null) {
      throw new IllegalArgumentException("Invalid Password and Message format");
    }
    log.info("Message: {}", message);

    HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    String requestId = requestHeader.getHeader("x-requestId");
    log.info("requestId: {}", requestId);

    Spake2PlusDeviceWorkFlow workFlow = workflowClient.newWorkflowStub(
      Spake2PlusDeviceWorkFlow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue("Spake2PlusTaskQueue")
        .setWorkflowId(requestId)
        .build());

    WorkflowClient.start(() -> {
      try {
        workFlow.startDeviceOwnerPairing();
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
    workFlow.receiveMessageFromVehicle(new DeviceMessagePayload(vin, message));
    return "SPAKE2+ for vehicle " + vin + " started successfully!";
  }

  private String signalWorkFlow(String requestId, String vin, String tlvMessage) {
    Spake2PlusDeviceWorkFlow workFlow = workflowClient.newWorkflowStub(Spake2PlusDeviceWorkFlow.class, requestId);
    workFlow.receiveMessageFromVehicle(new DeviceMessagePayload(vin, tlvMessage));
    return "Received a message from vehicle successfully " + tlvMessage;
  }
}