package com.xdman.workflow_vehicle.workflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface Spake2PlusVehicleWorkFlow {
  @WorkflowMethod
  String startVehicleWorkflow(String vin, String password, String salt);
  @SignalMethod
  void receiveMessageFromVehicle(String messagePayload);
}
