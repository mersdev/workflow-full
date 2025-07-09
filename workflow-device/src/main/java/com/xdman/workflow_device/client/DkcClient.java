package com.xdman.workflow_device.client;

import com.xdman.workflow_device.config.DkcFeignClientConfig;
import com.xdman.workflow_device.model.request.SendToVehicleRequest;
import com.xdman.workflow_device.model.response.SendToVehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
  name = "DkcClient",
  url = "http://localhost:3031",
  configuration = {
	DkcFeignClientConfig.class// Add any necessary configuration classes here
  },
  fallbackFactory = DkcFallbackFactory.class
)
public interface DkcClient {
  @PostMapping(path = "/sendToVehicle/{vin}")
  SendToVehicleResponse sendMessageToVehicle(
	@PathVariable("vin") String vin,
	@RequestBody SendToVehicleRequest messagePayload,
	@RequestHeader("x-requestId") String requestId
  );
}