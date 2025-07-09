package com.xdman.workflow_vehicle.client;


import com.xdman.workflow_vehicle.model.request.ReceivedFromVehicleRequest;
import com.xdman.workflow_vehicle.model.request.SendToVehicleRequest;
import com.xdman.workflow_vehicle.model.response.ReceivedFromVehicleResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class SbodFallbackFactory implements FallbackFactory<SbodClient> {

  @Override
  public SbodClient create(Throwable cause) {
    return new SbodClient() {

      @Override
      public ReceivedFromVehicleResponse receivedFromVehicle(String vin, ReceivedFromVehicleRequest messagePayload, String requestId) {
        return new ReceivedFromVehicleResponse("Fallback response: Unable to process message from vehicle for VIN: " + vin + " (requestId: " + requestId + ")");
      }
    };
  }
}
