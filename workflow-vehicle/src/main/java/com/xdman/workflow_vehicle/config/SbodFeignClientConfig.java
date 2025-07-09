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
		// Try to get x-requestId from current request context
		String requestId = null;
		try {
		  HttpServletRequest requestHeader = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		  requestId = requestHeader.getHeader("x-requestId");
		} catch (Exception e) {
		  // RequestContextHolder might not be available in some contexts (e.g., async operations)
		  // This is expected and we'll generate a new UUID below
		}

		// If no x-requestId found in current request, generate a new UUID
		if (requestId == null || requestId.trim().isEmpty()) {
		  requestId = UUID.randomUUID().toString();
		}

		String currentTime = String.valueOf(System.currentTimeMillis() / 1000);
		requestTemplate.header("x-requestId", requestId);
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