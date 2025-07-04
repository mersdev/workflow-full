package com.xdman.workflow_device.controllers;

import com.xdman.workflow_device.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_device.model.request.StartFullOwnerPairingRequest;
import com.xdman.workflow_device.model.response.ReceivedFromVehicleResponse;
import com.xdman.workflow_device.model.response.StartFullOwnerPairingResponse;
import com.xdman.workflow_device.service.ReceivedFromVehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceOEMController {
  @Autowired
  private ReceivedFromVehicleService receivedFromVehicleService;

  @PostMapping(value = "/startFullOwnerPairingCycle/{vin}")
  ResponseEntity<StartFullOwnerPairingResponse> receivedFromVehicle(
	@PathVariable("vin") String vin,
	@RequestBody StartFullOwnerPairingRequest request
  ) {
	String message = receivedFromVehicleService.startFullOwnerPairingCycle(vin, request.password(), request.salt());
	StartFullOwnerPairingResponse response = new StartFullOwnerPairingResponse(message);
	return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @PostMapping(value = "/receivefromvehicle/{vin}")
  ResponseEntity<ReceivedFromVehicleResponse> receivedFromVehicle(
	@PathVariable("vin") String vin,
	@RequestBody ReceivedFromVehicleRequest request
  ) throws Exception {
	String message = receivedFromVehicleService.receiveMessageFromVehicle(vin, request.message());
	ReceivedFromVehicleResponse response = new ReceivedFromVehicleResponse(message);
	return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
