package com.xdman.workflow_vehicle.client;


import com.xdman.workflow_vehicle.config.SbodFeignClientConfig;
import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
  name = "SbodClient",
  url = "${api.sbod.endpoint}",
  configuration = {SbodFeignClientConfig.class},
  fallbackFactory = SbodFallbackFactory.class
)
public interface SbodClient {
  @PostMapping(
	path = "/receivefromvehicle/{vin}"
  )
  ReceivedFromVehicleResponse receivedFromVehicle(
	@PathVariable("vin") String vin,
	@RequestBody ReceivedFromVehicleRequest messagePayload
  );
}