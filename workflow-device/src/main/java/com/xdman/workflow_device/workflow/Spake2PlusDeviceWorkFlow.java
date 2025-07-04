package com.xdman.workflow_device.workflow;

import com.xdman.workflow_device.model.DeviceMessagePayload;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface Spake2PlusDeviceWorkFlow {
  @WorkflowMethod
  void startDeviceOwnerPairing() throws Exception;
  @SignalMethod
  void receiveMessageFromVehicle(DeviceMessagePayload messagePayload);
}
