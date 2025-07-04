package com.xdman.workflow_vehicle.workflow;


import com.xdman.workflow_vehicle.activity.Spake2PlusDeviceActivity;
import com.xdman.workflow_vehicle.activity.Spake2PlusVehicleActivity;
import com.xdman.workflow_vehicle.config.WorkFlowConfig;
import com.xdman.workflow_vehicle.model.Spake2PlusDeviceData;
import com.xdman.workflow_vehicle.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_vehicle.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.SelectCommandTlv;
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
	String selectCommandTlv = vehicleActivity.createSelectCommandSuccessfully(vin);
	String status = vehicleActivity.sendSelectCommandSuccessfully(vin, selectCommandTlv);
	log.info("Send Select Command to Device: {}", status);

	Workflow.await(()-> !message.isEmpty());
	ResponseToSelectCommandTlv responseToSelectCommandTlv = vehicleActivity.receiveSelectResponseSuccessfully(message);
	log.info("Select Command Response: {}", responseToSelectCommandTlv);
	message = "";
	Spake2PlusRequestWrapper requestWrapper = vehicleActivity.createSpake2PlusRequestSuccessfully(password, salt);
	log.info("SPAKE2+ Request Command: {}", requestWrapper.request().encode());
	status = vehicleActivity.sendSpake2PlusRequestSuccessfully(vin, requestWrapper.request().encode());
	log.info("Send SPAKE2+ Request to Device: {}", status);

	Workflow.await(()-> !message.isEmpty());
	Spake2PlusRequestResponseTlv response = vehicleActivity.receiveSpake2PlusResponseSuccessfully(message);
	message = "";
	Spake2PlusVerifyCommandTlv verifyCommandTlv = vehicleActivity.createSpake2PlusVerifyCommandSuccessfully(
	  response,
	  requestWrapper.config()
	);
	log.info("SPAKE2+ Verify Command: {}", verifyCommandTlv.encode());
	status = vehicleActivity.sendSpake2PlusVerifyCommandSuccessfully(vin, verifyCommandTlv.encode());
	log.info("Send SPAKE2+ Verify Command to Device: {} ", status);

	Workflow.await(()-> !message.isEmpty());
	Spake2PlusVerifyResponseTlv verifyResponseTlv = vehicleActivity.receiveSpake2PlusVerifyResponseCommandSuccessfully(message);
	log.info("Verify Command Response: {}", verifyResponseTlv);
	return "Full Process for vehicle " + vin + " executed successfully!";
  }

  @Override
  public void receiveMessageFromVehicle(String messagePayload) {
	this.message = messagePayload;
  }
}
