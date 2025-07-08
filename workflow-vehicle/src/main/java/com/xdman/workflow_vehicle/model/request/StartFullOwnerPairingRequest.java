package com.xdman.workflow_vehicle.model.request;

public record StartFullOwnerPairingRequest(
  String password,
  String salt
) {
}
