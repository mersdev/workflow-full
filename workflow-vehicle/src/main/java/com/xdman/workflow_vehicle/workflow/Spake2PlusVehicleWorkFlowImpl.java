package com.xdman.workflow_vehicle.workflow;


import com.xdman.workflow_vehicle.activity.Spake2PlusDeviceActivity;
import com.xdman.workflow_vehicle.activity.Spake2PlusVehicleActivity;
import com.xdman.workflow_vehicle.config.WorkFlowConfig;
import com.xdman.workflow_vehicle.model.Spake2PlusDeviceData;
import com.xdman.workflow_vehicle.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusRequestResponseTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WorkflowImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusVehicleWorkFlowImpl implements Spake2PlusVehicleWorkFlow {
  private String message = "";
  private Spake2PlusDeviceData config;

  private final Spake2PlusVehicleActivity vehicleActivity = Workflow.newActivityStub(
	Spake2PlusVehicleActivity.class,
	WorkFlowConfig.defaultActivityOptions()
  );

  @Override
  public String startVehicleWorkflow(String vin, String password, String salt) {
	Spake2PlusRequestWrapper requestWrapper = vehicleActivity.createSpake2PlusRequestSuccessfully(password, salt);
	log.info("SPAKE2+ Request Command: " + requestWrapper.request().encode());
	String status = vehicleActivity.sendSpake2PlusRequestSuccessfully(vin, requestWrapper.request().encode());
	log.info("SPAKE2+ Request Response: " + status);
	message = "";
	Workflow.await(()-> !message.isEmpty());
	Spake2PlusRequestResponseTlv response = vehicleActivity.receiveSpake2PlusResponseSuccessfully(message);
	Spake2PlusVerifyCommandTlv verifyCommandTlv = vehicleActivity.createSpake2PlusVerifyCommandSuccessfully(
	  response,
	  requestWrapper.config()
	);
	status = vehicleActivity.sendSpake2PlusVerifyCommandSuccessfully(vin, verifyCommandTlv.encode());
	log.info("SPAKE2+ Verify Response: " + status);
	message = "";
	Workflow.await(()-> !message.isEmpty());
	Spake2PlusVerifyResponseTlv verifyResponseTlv = vehicleActivity.receiveSpake2PlusVerifyResponseCommandSuccessfully(message);
	return "Full Process for vehicle " + vin + " executed successfully!";
  }

  @Override
  public void receiveMessageFromVehicle(String messagePayload) {
	this.message = messagePayload;
  }
}
