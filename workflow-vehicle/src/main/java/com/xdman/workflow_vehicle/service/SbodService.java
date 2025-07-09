package com.xdman.workflow_vehicle.service;

import com.xdman.workflow_vehicle.client.SbodClient;
import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.RequestContext;

@Service
public class SbodService {
  private final SbodClient sbodClient;

  public SbodService(SbodClient sbodClient) {
	this.sbodClient = sbodClient;
  }

  public ReceivedFromVehicleResponse receiveFromVehicle(String vin, String messagePayload, String requestId) {
	ReceivedFromVehicleRequest request = new ReceivedFromVehicleRequest(messagePayload);
	return sbodClient.receivedFromVehicle(vin, request, requestId);
  }

  // Backward compatibility method - generates new UUID if no requestId provided
  public ReceivedFromVehicleResponse receiveFromVehicle(String vin, String messagePayload) {
	return receiveFromVehicle(vin, messagePayload, java.util.UUID.randomUUID().toString());
  }
}
