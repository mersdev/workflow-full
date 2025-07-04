package com.xdman.workflow_device.activity;

import com.xdman.workflow_device.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_device.model.Spake2PlusVehicleData;
import com.xdman.workflow_device.model.response.ReceivedFromVehicleResponse;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.SelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusRequestResponseTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import com.xdman.workflow_device.service.Spake2PlusVehicleService;
import io.temporal.activity.ActivityInterface;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ActivityImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusVehicleActivityImpl implements Spake2PlusVehicleActivity {
  private final Spake2PlusVehicleService spake2PlusVehicleService;

  public Spake2PlusVehicleActivityImpl(Spake2PlusVehicleService spake2PlusVehicleService) {
	this.spake2PlusVehicleService = spake2PlusVehicleService;
  }

  @Override
  public String createSelectCommandSuccessfully(String vin) {
	if (vin == null ) {
	  throw new IllegalArgumentException("Invalid VIN or Command ID format");
	}
	log.info("Creating select command for VIN: {}", vin);
	return new SelectCommandTlv().encode();
  }

  @Override
  public ResponseToSelectCommandTlv receiveSelectResponseSuccessfully(String message) {
	return new ResponseToSelectCommandTlv().decode(message);
  }

  @Override
  public Spake2PlusRequestWrapper createSpake2PlusRequestSuccessfully(String password, String salt) {
	return spake2PlusVehicleService.createSpake2PlusRequest(password, salt);
  }

  @Override
  public Spake2PlusRequestResponseTlv receiveSpake2PlusRequestSuccessfully(String message) {
	return new Spake2PlusRequestResponseTlv().decode(message);
  }

  @Override
  public Spake2PlusVerifyCommandTlv createSpake2PlusVerifyCommandSuccessfully(
	Spake2PlusRequestResponseTlv spake2PlusRequestResponseTlv,
	Spake2PlusVehicleData config
  ) {
	return spake2PlusVehicleService.validateSpake2PlusRequest(spake2PlusRequestResponseTlv, config);
  }

  @Override
  public Spake2PlusVerifyResponseTlv receiveSpake2PlusVerifyCommandSuccessfully(String message) {
	return new Spake2PlusVerifyResponseTlv().decode(message);
  }
}
