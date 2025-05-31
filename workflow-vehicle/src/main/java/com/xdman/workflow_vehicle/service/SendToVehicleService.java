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

  public String sendToVehicle(String vin, String message){
    if (message == null) {
      throw new IllegalArgumentException("Invalid message format");
    }

    log.info("Received message from vehicle VIN: {}, Message: {}", vin, message);
    log.info("Verify command detected, signaling workflow");
    HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    String requestId = requestHeader.getHeader("x-requestId");
    log.info("requestId for signaling: {}", requestId);
    return signalWorkFlow(requestId, message);
  }

  public String startOwnerPairingCycle(String vin, String password, String salt) {
    if (password == null || salt == null) {
      throw new IllegalArgumentException("Invalid Password and Salt format");
    }

    HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    String requestId = requestHeader.getHeader("x-requestId");

    Spake2PlusVehicleWorkFlow workFlow = workflowClient.newWorkflowStub(
      Spake2PlusVehicleWorkFlow.class,
      WorkflowOptions.newBuilder()
        .setTaskQueue("Spake2PlusTaskQueue")
        .setWorkflowId(requestId)
        .build());
    return workFlow.startVehicleWorkflow(vin, password, salt);
  }

  private String signalWorkFlow(String requestId, String tlvMessage) {
    Spake2PlusVehicleWorkFlow workFlow = workflowClient.newWorkflowStub(Spake2PlusVehicleWorkFlow.class, requestId);
    workFlow.receiveMessageFromVehicle(tlvMessage);
    return "Received a message from vehicle successfully " + tlvMessage;
  }
}