package com.xdman.workflow_vehicle.model;


import com.xdman.workflow_vehicle.model.tlv.Spake2PlusRequestCommandTlv;

public record Spake2PlusRequestWrapper(
  Spake2PlusRequestCommandTlv request,
  Spake2PlusVehicleData config
) { }
