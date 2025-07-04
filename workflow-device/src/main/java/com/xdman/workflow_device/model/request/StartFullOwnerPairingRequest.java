package com.xdman.workflow_device.model.request;

public record StartFullOwnerPairingRequest(
  String password,
  String salt
) {
}
