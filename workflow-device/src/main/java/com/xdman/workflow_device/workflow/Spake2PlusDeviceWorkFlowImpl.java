package com.xdman.workflow_device.workflow;

import com.xdman.workflow_device.activity.Spake2PlusDeviceActivity;
import com.xdman.workflow_device.activity.Spake2PlusVehicleActivity;
import com.xdman.workflow_device.config.WorkFlowConfig;
import com.xdman.workflow_device.model.DeviceMessagePayload;
import com.xdman.workflow_device.model.Spake2PlusDeviceData;
import com.xdman.workflow_device.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_device.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.SelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusRequestCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WorkflowImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusDeviceWorkFlowImpl implements Spake2PlusDeviceWorkFlow {
  private DeviceMessagePayload message;
  private Spake2PlusDeviceData config;

  private final Spake2PlusDeviceActivity deviceActivity = Workflow.newActivityStub(
	Spake2PlusDeviceActivity.class,
	WorkFlowConfig.defaultActivityOptions()
  );

  private final Spake2PlusVehicleActivity vehicleActivity = Workflow.newActivityStub(
	Spake2PlusVehicleActivity.class,
	WorkFlowConfig.defaultActivityOptions()
  );

  @Override
  public void startDeviceOwnerPairing() {
	String status = "";

	Workflow.await(() -> message != null &&
	  message.message() != null &&
	  (message.message().contains("00A40400") || !message.message().isEmpty()));
	String selectCommand = message.message();
	SelectCommandTlv selectCommandTlv = deviceActivity.receiveSelectCommandSuccessfully(message.message());
	ResponseToSelectCommandTlv responseToSelectCommandTlv = deviceActivity.processSelectCommandSuccessfully(selectCommandTlv);
	try {
	  status = deviceActivity.sendSelectResponseSuccessfully(message.vin(), responseToSelectCommandTlv.encode());
	  log.info("Sending Select Response {}", status);
	} catch (Exception e) {
	  log.error("Failed to publish command message to DKC", e);
	  throw new RuntimeException(e);
	}

	Workflow.await(() -> message.message().contains("803000")|| !message.message().equals(selectCommand));
	String spake2PlusRequestCommand = message.message();
	Spake2PlusRequestCommandTlv request = deviceActivity.receiveSpake2PlusRequestCommandSuccessfully(message.message());

	Spake2PlusResponseWrapper response = deviceActivity.processSpake2PlusRequestSuccessfully(request, "0102030405060708090A0B0C0D0E0F10");
	try {
	  status = deviceActivity.sendSpake2PlusResponseSuccessfully(message.vin(), response.response().encode());
	  log.info("Sending Response {}", status);
	} catch (Exception e) {
	  log.error("Failed to publish command message to DKC", e);
	  throw new RuntimeException(e);
	}
	log.info("Received message from vehicle VIN: {}", message.message());
	Workflow.await(() -> message.message().contains("803200")|| !message.message().equals(spake2PlusRequestCommand));
	Spake2PlusVerifyCommandTlv verifyCommandTlv = deviceActivity.receiveSpake2PlusVerifyCommandSuccessfully(message.message());
	Spake2PlusVerifyResponseTlv verifyResponseTlv = deviceActivity.processSpake2PlusVerifyCommandSuccessfully(verifyCommandTlv, response.config());
	try {
	  status = deviceActivity.sendSpake2PlusVerifyResponseSuccessfully(message.vin(), verifyResponseTlv.encode());
	  log.info("Sending Verify Response {}", status);
	} catch (Exception e) {
	  log.error("Failed to publish command message to DKC", e);
	  throw new RuntimeException(e);
	}
	message = null;
  }

  @Override
  public void receiveMessageFromVehicle(DeviceMessagePayload messagePayload) {
	this.message = messagePayload;
  }
}
