package com.xdman.workflow_device.model;


import com.xdman.workflow_device.model.tlv.Spake2PlusRequestCommandTlv;

public record Spake2PlusRequestWrapper(
  Spake2PlusRequestCommandTlv request,
  Spake2PlusVehicleData config
) { }
