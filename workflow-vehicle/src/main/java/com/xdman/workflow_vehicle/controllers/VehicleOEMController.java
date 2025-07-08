package com.xdman.workflow_vehicle.controllers;

import com.xdman.workflow_vehicle.model.request.SendToVehicleRequest;
import com.xdman.workflow_vehicle.model.request.StartFullOwnerPairingRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import com.xdman.workflow_vehicle.model.response.SendToVehicleResponse;
import com.xdman.workflow_vehicle.model.response.StartFullOwnerPairingResponse;
import com.xdman.workflow_vehicle.service.SbodService;
import com.xdman.workflow_vehicle.service.SendToVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VehicleOEMController {
  @Autowired
  private SendToVehicleService sendToVehicleService;
  @Autowired
  private SbodService sbodService;

  @PostMapping(value = "/startFullOwnerPairingCycle/{vin}")
  ResponseEntity<StartFullOwnerPairingResponse> receivedFromVehicle(
	@PathVariable("vin") String vin,
	@RequestBody StartFullOwnerPairingRequest request
  ) {
	String message = sendToVehicleService.startFullOwnerPairingCycle(vin, request.password(), request.salt());
	StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);
	return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/sendToVehicle/{vin}")
  ResponseEntity<SendToVehicleResponse> sendToVehicle(
	@PathVariable("vin") String vin,
	@RequestBody SendToVehicleRequest request
	){
	String message = sendToVehicleService.sendToVehicle(vin, request.messagePayload());
	SendToVehicleResponse response = new SendToVehicleResponse(message);
	return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/startOwnerPairing/{vin}")
  ResponseEntity<SendToVehicleResponse> startOwnerPairing(
	@PathVariable("vin") String vin,
	@RequestBody StartFullOwnerPairingRequest request
  ){
	String message = sendToVehicleService.startOwnerPairingCycle(vin, request.password(), request.salt());
	SendToVehicleResponse response = new SendToVehicleResponse(message);
	return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/testSbodReceiveFromVehicle/{vin}")
  ResponseEntity<ReceivedFromVehicleResponse> testSbodReceiveFromVehicle(@PathVariable String vin){
	ReceivedFromVehicleResponse response = sbodService.receiveFromVehicle(vin, "803000002F5B0201005C0201007F5020C0100102030405060708090A0B0C0D0E0F10C10400001000C2020008C3020001D602000300");
	return new ResponseEntity<>(response, HttpStatus.OK);
  }

}
