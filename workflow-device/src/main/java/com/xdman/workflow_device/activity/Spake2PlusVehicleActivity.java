package com.xdman.workflow_device.activity;


import com.xdman.workflow_device.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_device.model.Spake2PlusVehicleData;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusRequestResponseTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface Spake2PlusVehicleActivity {
  String createSelectCommandSuccessfully(String vin);
  ResponseToSelectCommandTlv receiveSelectResponseSuccessfully(String message);
  Spake2PlusRequestWrapper createSpake2PlusRequestSuccessfully(String password, String salt);
  Spake2PlusRequestResponseTlv receiveSpake2PlusRequestSuccessfully(String message);
  Spake2PlusVerifyCommandTlv createSpake2PlusVerifyCommandSuccessfully(
	Spake2PlusRequestResponseTlv spake2PlusRequestResponseTlv,
	Spake2PlusVehicleData config
  );
  Spake2PlusVerifyResponseTlv receiveSpake2PlusVerifyCommandSuccessfully(String message);
}
