package com.xdman.workflow_device.model;

public record DeviceMessagePayload(String vin, String message) {
	public DeviceMessagePayload {
		if (vin == null || vin.isBlank()) {
			throw new IllegalArgumentException("VIN cannot be null or blank");
		}
		if (message == null || message.isBlank()) {
			throw new IllegalArgumentException("Message cannot be null or blank");
		}
	}

}
