package com.xdman.workflow_device.workflow;

import com.xdman.workflow_device.activity.Spake2PlusDeviceActivity;
import com.xdman.workflow_device.activity.Spake2PlusVehicleActivity;
import com.xdman.workflow_device.config.WorkFlowConfig;
import com.xdman.workflow_device.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_device.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.SelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WorkflowImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusFullWorkFlowImpl implements Spake2PlusFullWorkFlow {

  private final Spake2PlusDeviceActivity deviceActivity = Workflow.newActivityStub(
    Spake2PlusDeviceActivity.class,
    WorkFlowConfig.defaultActivityOptions()
  );

  private final Spake2PlusVehicleActivity vehicleActivity = Workflow.newActivityStub(
    Spake2PlusVehicleActivity.class,
    WorkFlowConfig.defaultActivityOptions()
  );

  @Override
  public String processFullCycleOwnerPairing(String vin,String password, String salt) {
    String selectCommandTlv = vehicleActivity.createSelectCommandSuccessfully(vin);
    ResponseToSelectCommandTlv responseToSelectCommandTlv = deviceActivity.processSelectCommandSuccessfully(new SelectCommandTlv().decode(selectCommandTlv));
    log.info("Select Command Response: " + responseToSelectCommandTlv.encode());
    Spake2PlusRequestWrapper requestWrapper = vehicleActivity.createSpake2PlusRequestSuccessfully(password, salt);
    log.info("SPAKE2+ Request: " + requestWrapper.request().encode());
    Spake2PlusResponseWrapper responseWrapper = deviceActivity.processSpake2PlusRequestSuccessfully(requestWrapper.request(), password);
    Spake2PlusVerifyCommandTlv verifyCommandTlv = vehicleActivity.createSpake2PlusVerifyCommandSuccessfully(
      responseWrapper.response(),
      requestWrapper.config()
    );
    log.info("SPAKE2+ Verify: " + verifyCommandTlv.encode());
    Spake2PlusVerifyResponseTlv responseTlv = deviceActivity.processSpake2PlusVerifyCommandSuccessfully(
      verifyCommandTlv,
      responseWrapper.config()
    );
	return "SPAKE2+ for vehicle " + vin + " execute successfully!";
  }

}
