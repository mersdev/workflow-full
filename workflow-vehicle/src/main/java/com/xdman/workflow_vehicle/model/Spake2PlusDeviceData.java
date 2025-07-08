package com.xdman.workflow_vehicle.model;

import java.math.BigInteger;

public record Spake2PlusDeviceData(
  String password,
  BigInteger w0,
  BigInteger w1,
  BigInteger x
) {
}
