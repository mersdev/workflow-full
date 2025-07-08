package com.xdman.workflow_vehicle.config;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.UUID;

public class SbodFeignClientConfig {
  @Bean
  public RequestInterceptor requestInterceptorWithDKCHeader() {
	return requestTemplate -> {
	  try {
//		HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//		String requestId = requestHeader.getHeader("x-requestId");
//		UUID uuid = UUID.randomUUID();
		String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
		requestTemplate.header("x-requestId", "7d89f678-test-4b51-b4d7a");
//		requestTemplate.header("x-fmsId", "DKC-Test");
//		requestTemplate.header("timestamp", currentTime);
//		requestTemplate.header("x-sbodId", "workflow");
		requestTemplate.header(HttpHeaders.CONTENT_TYPE, "application/json");
	  } catch (Exception e) {
		throw new RuntimeException(e);
	  }
	};
  }

}