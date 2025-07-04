package com.xdman.workflow_device.config;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;

import java.time.Duration;

public class WorkFlowConfig {
  public static RetryOptions retryoptions = RetryOptions.newBuilder()
	.setInitialInterval(Duration.ofSeconds(1))
	.setMaximumInterval(Duration.ofSeconds(5))
	.setBackoffCoefficient(2)
	.setMaximumAttempts(2)
	.build();

  public static WorkflowOptions getWorkflowOptions(String taskQueue, String workflowId) {
	var builder = WorkflowOptions.newBuilder();
	builder.setWorkflowId(workflowId);
	builder.setTaskQueue(taskQueue);
	builder.setWorkflowRunTimeout(java.time.Duration.ofMinutes(2));
	builder.setWorkflowTaskTimeout(java.time.Duration.ofMinutes(1));

	return builder.build();
  }

  public static ActivityOptions defaultActivityOptions() {
	return
	  ActivityOptions.newBuilder()
		// Timeout options specify when to automatically timeout Activities if the process is taking too long.
		.setStartToCloseTimeout(Duration.ofSeconds(10))
		.setRetryOptions(retryoptions)
		.build();
  }
}
