package com.xdman.workflow_vehicle.workflow;

import com.xdman.workflow_vehicle.activity.Spake2PlusDeviceActivity;
import com.xdman.workflow_vehicle.activity.Spake2PlusVehicleActivity;
import com.xdman.workflow_vehicle.config.WorkFlowConfig;
import com.xdman.workflow_vehicle.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_vehicle.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_vehicle.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.SelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyResponseTlv;
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
	log.info("Select Command: {}", selectCommandTlv);
    ResponseToSelectCommandTlv responseToSelectCommandTlv = deviceActivity.processSelectCommandSuccessfully(new SelectCommandTlv().decode(selectCommandTlv));
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
