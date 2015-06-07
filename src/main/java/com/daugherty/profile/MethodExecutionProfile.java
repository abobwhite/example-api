package com.daugherty.profile;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.daugherty.e2c.domain.ValueObject;

/**
 * Represents a profile of method executions.
 */
public class MethodExecutionProfile extends ValueObject {

    private final String methodName;
    private final Integer maximumSampleSize; // TODO Should be in subclass?
    private final List<Long> sampleElapsedTimes = new ArrayList<Long>();
    private Long totalTime;
    private Long minimumTime;
    private Long maximumTime;

    public MethodExecutionProfile(String methodName, Integer sampleSize) {
        this.methodName = methodName;
        maximumSampleSize = sampleSize;
    }

    public String getMethodName() {
        return methodName;
    }

    public void recordElapsedTime(long elapsedTime) {
        addElapsedTimeToSample(elapsedTime);
        updateTotalTime(elapsedTime);
        updateMinimumTime(elapsedTime);
        updateMaximumTime(elapsedTime);
    }

    private void addElapsedTimeToSample(long elapsedTime) {
        synchronized (sampleElapsedTimes) {
            sampleElapsedTimes.add(elapsedTime);
            if (sampleElapsedTimes.size() > maximumSampleSize) {
                sampleElapsedTimes.remove(0);
            }
        }
    }

    private void updateTotalTime(long elapsedTime) {
        if (totalTime == null) {
            totalTime = new Long(0);
        }
        totalTime += elapsedTime;
    }

    private void updateMinimumTime(long elapsedTime) {
        if (minimumTime == null || minimumTime > elapsedTime) {
            minimumTime = elapsedTime;
        }
    }

    private void updateMaximumTime(long elapsedTime) {
        if (maximumTime == null || maximumTime < elapsedTime) {
            maximumTime = elapsedTime;
        }
    }

    public Double getAverageTime() {
        if (sampleElapsedTimes.isEmpty()) {
            return null;
        }

        Double sampleTotalTime = new Double(0);
        for (Long time : sampleElapsedTimes) {
            sampleTotalTime += time;
        }
        return sampleTotalTime / sampleElapsedTimes.size();
    }

    public Double getStandardDeviation() {
        if (sampleElapsedTimes.isEmpty()) {
            return null;
        } else if (sampleElapsedTimes.size() == 1) {
            return new Double(0);
        }

        Double averageTime = getAverageTime();
        Double quadraticSum = new Double(0);
        for (Long time : sampleElapsedTimes) {
            quadraticSum += (time - averageTime) * (time - averageTime);
        }
        // This is a sample, not a full population, so we subtract 1 from sample size
        Double variance = quadraticSum / (sampleElapsedTimes.size() - 1);
        return Math.sqrt(variance);
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public Long getMinimumTime() {
        return minimumTime;
    }

    public Long getMaximumTime() {
        return maximumTime;
    }

    @Override
    protected void addFieldsToEqualsBuilder(EqualsBuilder builder, Object obj) {
        MethodExecutionProfile other = (MethodExecutionProfile) obj;
        builder.append(methodName, other.methodName);
    }

    @Override
    protected int hashCodeMultiplier() {
        return 1111;
    }

    @Override
    protected void addFieldsToHashCodeBuilder(HashCodeBuilder builder) {
        builder.append(methodName);
    }

    @Override
    protected void addFieldsToToStringBuilder(ToStringBuilder builder) {
        builder.append("methodName", methodName).append("average", getAverageTime()).append("total", totalTime)
                .append("minimum", minimumTime).append("maximum", maximumTime);
    }
}
