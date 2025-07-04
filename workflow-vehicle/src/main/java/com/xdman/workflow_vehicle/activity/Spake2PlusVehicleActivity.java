package com.xdman.workflow_vehicle.activity;

import com.xdman.workflow_vehicle.model.Spake2PlusRequestWrapper;
import com.xdman.workflow_vehicle.model.Spake2PlusVehicleData;
import com.xdman.workflow_vehicle.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.SelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusRequestResponseTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface Spake2PlusVehicleActivity {
  String createSelectCommandSuccessfully(String vin);
  String sendSelectCommandSuccessfully(String vin, String selectCommand);
  ResponseToSelectCommandTlv receiveSelectResponseSuccessfully(String message);
  Spake2PlusRequestWrapper createSpake2PlusRequestSuccessfully(String password, String salt);
  String sendSpake2PlusRequestSuccessfully(String vin, String spake2PlusRequestCommand);
  Spake2PlusRequestResponseTlv receiveSpake2PlusResponseSuccessfully(String message);
  Spake2PlusVerifyCommandTlv createSpake2PlusVerifyCommandSuccessfully(
	Spake2PlusRequestResponseTlv spake2PlusRequestResponseTlv,
	Spake2PlusVehicleData config
  );
  String sendSpake2PlusVerifyCommandSuccessfully(String vin, String spake2PlusVerifyCommand);
  Spake2PlusVerifyResponseTlv receiveSpake2PlusVerifyResponseCommandSuccessfully(String message);
}
