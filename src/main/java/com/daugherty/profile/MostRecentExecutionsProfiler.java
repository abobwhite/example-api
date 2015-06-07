package com.daugherty.profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Profiler that tracks the most recent set of executions for each method.
 */
public class MostRecentExecutionsProfiler implements MethodExecutionProfileRecorder, MethodExecutionProfileReporter {

    private Integer sampleSize = Integer.MAX_VALUE;
    private final Map<String, MethodExecutionProfile> profileByMethodMap = new ConcurrentHashMap<String, MethodExecutionProfile>();

    public void setSampleSize(Integer sampleSize) {
        this.sampleSize = sampleSize;
    }

    @Override
    public void recordExecution(String methodName, long elapsedTime) {
        MethodExecutionProfile profile = profileByMethodMap.get(methodName);
        if (profile == null) {
            // TODO Use subclass of MethodExecutionProfile that limits sample size?
            profile = new MethodExecutionProfile(methodName, sampleSize);
            profileByMethodMap.put(methodName, profile);
        }
        profile.recordElapsedTime(elapsedTime);
    }

    @Override
    public List<MethodExecutionProfile> getProfiles(Comparator<MethodExecutionProfile> comparator) {
        List<MethodExecutionProfile> profiles = new ArrayList<MethodExecutionProfile>();
        profiles.addAll(profileByMethodMap.values());
        Collections.sort(profiles, comparator);
        return Collections.unmodifiableList(profiles);
    }

}
