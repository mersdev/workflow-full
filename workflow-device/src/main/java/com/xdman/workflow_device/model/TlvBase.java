package com.xdman.workflow_device.model;

public abstract class TlvBase {

  public abstract Object decode(String tlvString);

  public abstract String encode();
}
