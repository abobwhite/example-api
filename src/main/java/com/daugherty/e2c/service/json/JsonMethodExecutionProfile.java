package com.daugherty.e2c.service.json;

import com.daugherty.profile.MethodExecutionProfile;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * Marshalls/unmarshalls MethodExecutionProfile objects to/from JSON..
 */
@JsonRootName(value = "methodExecutionProfile")
public class JsonMethodExecutionProfile {

    @JsonProperty("methodName")
    private String methodName;
    @JsonProperty("averageTime")
    private Double averageTime;
    @JsonProperty("totalTime")
    private Long totalTime;
    @JsonProperty("minimumTime")
    private Long minimumTime;
    @JsonProperty("maximumTime")
    private Long maximumTime;
    @JsonProperty("standardDeviation")
    private Double standardDeviation;

    public JsonMethodExecutionProfile(MethodExecutionProfile profile) {
        methodName = profile.getMethodName();
        averageTime = profile.getAverageTime();
        totalTime = profile.getTotalTime();
        minimumTime = profile.getMinimumTime();
        maximumTime = profile.getMaximumTime();
        standardDeviation = profile.getStandardDeviation();
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Double getAverageTime() {
        return averageTime;
    }

    public void setAverageTime(Double averageTime) {
        this.averageTime = averageTime;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Long getMinimumTime() {
        return minimumTime;
    }

    public void setMinimumTime(Long minimumTime) {
        this.minimumTime = minimumTime;
    }

    public Long getMaximumTime() {
        return maximumTime;
    }

    public void setMaximumTime(Long maximumTime) {
        this.maximumTime = maximumTime;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

}
