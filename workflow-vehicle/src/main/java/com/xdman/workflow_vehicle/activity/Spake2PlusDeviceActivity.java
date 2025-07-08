package com.xdman.workflow_vehicle.activity;

import com.xdman.workflow_vehicle.model.Spake2PlusDeviceData;
import com.xdman.workflow_vehicle.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_vehicle.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.SelectCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusRequestCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_vehicle.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface Spake2PlusDeviceActivity {
  SelectCommandTlv receiveSelectCommandSuccessfully(String message);
  ResponseToSelectCommandTlv processSelectCommandSuccessfully(SelectCommandTlv request);
  Spake2PlusRequestCommandTlv receiveSpake2PlusRequestCommandSuccessfully(String message);
  Spake2PlusResponseWrapper processSpake2PlusRequestSuccessfully(Spake2PlusRequestCommandTlv request, String password);
  Spake2PlusVerifyCommandTlv receiveSpake2PlusVerifyCommandSuccessfully(String message);
  Spake2PlusVerifyResponseTlv processSpake2PlusVerifyCommandSuccessfully(Spake2PlusVerifyCommandTlv request, Spake2PlusDeviceData config);
}
