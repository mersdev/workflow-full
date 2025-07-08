package com.xdman.workflow_device.model;


import com.xdman.workflow_device.model.tlv.Spake2PlusRequestResponseTlv;

public record Spake2PlusResponseWrapper(
  Spake2PlusRequestResponseTlv response,
  Spake2PlusDeviceData config
) {
}
