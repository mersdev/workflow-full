package com.xdman.workflow_device.service;

import com.xdman.workflow_device.client.DkcClient;
import com.xdman.workflow_device.model.request.SendToVehicleRequest;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import org.springframework.stereotype.Service;

@Service
public class DkcService  {
  private final DkcClient dkcClient;

  public DkcService(DkcClient dkcClient) {
	this.dkcClient = dkcClient;
  }

  public String publishCommandMessageToDkc(String vin, String commandMessage) throws Exception {
	SendToVehicleResponse response = dkcClient.sendMessageToVehicle(vin, new SendToVehicleRequest(commandMessage));
	return response.message();
  }

}
