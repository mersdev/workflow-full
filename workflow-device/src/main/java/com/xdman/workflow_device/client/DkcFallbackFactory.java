package com.xdman.workflow_device.client;

import com.xdman.workflow_device.model.request.SendToVehicleRequest;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DkcFallbackFactory implements FallbackFactory<DkcClient> {
  @Override
  public DkcClient create(Throwable cause) {
	return (vin, messagePayload, requestId) -> new SendToVehicleResponse("Fallback response: Unable to send message to vehicle with VIN " + vin + " (requestId: " + requestId + "). Cause: " + cause.getMessage());
  }
}
