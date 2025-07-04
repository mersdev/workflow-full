package com.xdman.workflow_device;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WorkflowDeviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorkflowDeviceApplication.class, args);
	}

}
