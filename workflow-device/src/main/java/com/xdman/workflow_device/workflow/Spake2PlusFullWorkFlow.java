package com.xdman.workflow_device.workflow;

import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface Spake2PlusFullWorkFlow {
  @WorkflowMethod
  String processFullCycleOwnerPairing(String vin, String password, String salt);
}
