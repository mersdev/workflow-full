package com.xdman.workflow_device.activity;


import com.xdman.workflow_device.model.Spake2PlusDeviceData;
import com.xdman.workflow_device.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.SelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusRequestCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface Spake2PlusDeviceActivity {
  SelectCommandTlv receiveSelectCommandSuccessfully(String message);
  ResponseToSelectCommandTlv processSelectCommandSuccessfully(SelectCommandTlv request);
  String sendSelectResponseSuccessfully(String vin, String message) throws Exception;
  Spake2PlusRequestCommandTlv receiveSpake2PlusRequestCommandSuccessfully(String message);
  Spake2PlusResponseWrapper processSpake2PlusRequestSuccessfully(Spake2PlusRequestCommandTlv request, String password);
  String sendSpake2PlusResponseSuccessfully(String vin, String message) throws Exception;
  Spake2PlusVerifyCommandTlv receiveSpake2PlusVerifyCommandSuccessfully(String message);
  Spake2PlusVerifyResponseTlv processSpake2PlusVerifyCommandSuccessfully(Spake2PlusVerifyCommandTlv request, Spake2PlusDeviceData config);
  String sendSpake2PlusVerifyResponseSuccessfully(String vin, String message) throws Exception;
}
