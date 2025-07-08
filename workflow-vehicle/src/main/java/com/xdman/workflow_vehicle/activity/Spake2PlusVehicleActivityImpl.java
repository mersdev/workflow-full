package com.xdman.workflow_vehicle.activity;

import com.xdman.workflow_vehicle.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_vehicle.model.Spake2PlusVehicleData;
import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import com.xdman.workflow_vehicle.model.response.SendToVehicleResponse;
import com.xdman.workflow_vehicle.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.SelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusRequestResponseTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyResponseTlv;
import com.xdman.workflow_vehicle.service.SbodService;
import com.xdman.workflow_vehicle.service.Spake2PlusVehicleService;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ActivityImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusVehicleActivityImpl implements Spake2PlusVehicleActivity {
  private final Spake2PlusVehicleService spake2PlusVehicleService;
  private final SbodService sbodService;

  public Spake2PlusVehicleActivityImpl(Spake2PlusVehicleService spake2PlusVehicleService, SbodService sbodService) {
	this.spake2PlusVehicleService = spake2PlusVehicleService;
	this.sbodService = sbodService;
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
  public String sendSelectCommandSuccessfully(String vin, String selectCommand) {
	ReceivedFromVehicleResponse response = sbodService.receiveFromVehicle(vin, selectCommand);
	return response.message();
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
  public String sendSpake2PlusRequestSuccessfully(String vin, String spake2PlusRequestCommand) {
	ReceivedFromVehicleResponse response = sbodService.receiveFromVehicle(vin,spake2PlusRequestCommand);
	return response.message();
  }

  @Override
  public Spake2PlusRequestResponseTlv receiveSpake2PlusResponseSuccessfully(String message) {
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
  public String sendSpake2PlusVerifyCommandSuccessfully(String vin, String spake2PlusVerifyCommand) {
	ReceivedFromVehicleResponse request = sbodService.receiveFromVehicle(vin, spake2PlusVerifyCommand);
	return request.message();
  }

  @Override
  public Spake2PlusVerifyResponseTlv receiveSpake2PlusVerifyResponseCommandSuccessfully(String message) {
	return new Spake2PlusVerifyResponseTlv().decode(message);
  }
}
