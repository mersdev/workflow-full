package com.xdman.workflow_device.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SendToVehicleRequest(
  @JsonProperty("message")
  String messagePayload
) {
}