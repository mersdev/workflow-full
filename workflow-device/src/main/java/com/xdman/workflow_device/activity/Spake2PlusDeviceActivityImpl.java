package com.xdman.workflow_device.activity;


import com.xdman.workflow_device.model.Spake2PlusDeviceData;
import com.xdman.workflow_device.model.Spake2PlusResponseWrapper;
import com.xdman.workflow_device.model.tlv.ResponseToSelectCommandTlv;
import com.xdman.workflow_device.model.tlv.SelectCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusRequestCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyCommandTlv;
import com.xdman.workflow_device.model.tlv.Spake2PlusVerifyResponseTlv;
import com.xdman.workflow_device.service.DkcService;
import com.xdman.workflow_device.service.Spake2PlusDeviceService;
import io.temporal.spring.boot.ActivityImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ActivityImpl(taskQueues = "Spake2PlusTaskQueue")
public class Spake2PlusDeviceActivityImpl implements Spake2PlusDeviceActivity {
  private final Spake2PlusDeviceService spake2PlusDeviceService;
  private final DkcService dkcService;

  public Spake2PlusDeviceActivityImpl(Spake2PlusDeviceService spake2PlusDeviceService, DkcService dkcService) {
	this.spake2PlusDeviceService = spake2PlusDeviceService;
	this.dkcService = dkcService;
  }
  @Override
  public SelectCommandTlv receiveSelectCommandSuccessfully(String message) {
	return new SelectCommandTlv().decode(message);
  }

  @Override
  public ResponseToSelectCommandTlv processSelectCommandSuccessfully(SelectCommandTlv request) {
	ResponseToSelectCommandTlv response = new ResponseToSelectCommandTlv();
	response.setFrameworkVersions(new byte[]{0x01, 0x00});
	response.setProtocolVersions(new byte[]{0x01, 0x00});
	response.setPairingMode(ResponseToSelectCommandTlv.PAIRING_MODE_STARTED_WITH_PASSWORD);
	return response;
  }

  @Override
  public String sendSelectResponseSuccessfully(String vin, String message) throws Exception {
	return dkcService.publishCommandMessageToDkc(vin, message);
  }

  @Override
  public Spake2PlusRequestCommandTlv receiveSpake2PlusRequestCommandSuccessfully(String message) {
	return new Spake2PlusRequestCommandTlv().decode(message);
  }

  @Override
  public Spake2PlusResponseWrapper processSpake2PlusRequestSuccessfully(Spake2PlusRequestCommandTlv request, String password) {
	return spake2PlusDeviceService.processSpake2PlusRequest(request, password);
  }

  @Override
  public String sendSpake2PlusResponseSuccessfully(String vin, String message) throws Exception {
	return dkcService.publishCommandMessageToDkc(vin, message);
  }

  @Override
  public Spake2PlusVerifyCommandTlv receiveSpake2PlusVerifyCommandSuccessfully(String message) {
	return new Spake2PlusVerifyCommandTlv().decode(message);
  }

  @Override
  public Spake2PlusVerifyResponseTlv processSpake2PlusVerifyCommandSuccessfully(Spake2PlusVerifyCommandTlv request, Spake2PlusDeviceData config) {
	return spake2PlusDeviceService.processSpake2PlusVerifyRequest(request, config);
  }

  @Override
  public String sendSpake2PlusVerifyResponseSuccessfully(String vin, String message) throws Exception {
	return dkcService.publishCommandMessageToDkc(vin, message);
  }
}
