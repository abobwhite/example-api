package com.daugherty.profile;

/**
 * Defines operations for recording method executions within an application.
 */
public interface MethodExecutionProfileRecorder {

    void recordExecution(String methodName, long elapsedTime);

}
