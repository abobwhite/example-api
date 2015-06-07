package com.daugherty.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MostRecentExecutionsProfilerTest {

    private MostRecentExecutionsProfiler profiler;
    private Comparator<MethodExecutionProfile> descendingAverageTimeComparator;

    @Before
    public void setUpFixtures() {
        profiler = new MostRecentExecutionsProfiler();

        descendingAverageTimeComparator = new Comparator<MethodExecutionProfile>() {
            @Override
            public int compare(MethodExecutionProfile profile1, MethodExecutionProfile profile2) {
                return -profile1.getAverageTime().compareTo(profile2.getAverageTime());
            }
        };
    }

    @Test
    public void getProfilesAfterNoExecutions() throws Exception {
        List<MethodExecutionProfile> profiles = profiler.getProfiles(descendingAverageTimeComparator);
        assertNotNull("Profiles collection must exist", profiles);
        assertTrue("Profiles collection must be empty", profiles.isEmpty());
    }

    @Test
    public void getProfilesAfterExecutionOfOneMethod() throws Exception {
        profiler.recordExecution("methodOne", 100);
        List<MethodExecutionProfile> profiles = profiler.getProfiles(descendingAverageTimeComparator);
        assertEquals(1, profiles.size());
        assertEquals("methodOne", profiles.get(0).getMethodName());
    }

    @Test
    public void getProfilesAfterSeveralExecutionsOfOneMethodNotExceedingSampleSize() throws Exception {
        profiler.recordExecution("methodOne", 100);
        profiler.recordExecution("methodOne", 120);
        profiler.recordExecution("methodOne", 140);
        List<MethodExecutionProfile> profiles = profiler.getProfiles(descendingAverageTimeComparator);
        assertEquals(1, profiles.size());
        assertEquals("methodOne", profiles.get(0).getMethodName());
        assertEquals(new Double(120), profiles.get(0).getAverageTime());
    }

    @Test
    public void getProfilesAfterSeveralExecutionsOfOneMethodExceedingSampleSize() throws Exception {
        profiler.setSampleSize(3);
        profiler.recordExecution("methodOne", 100);
        profiler.recordExecution("methodOne", 120);
        profiler.recordExecution("methodOne", 140);
        profiler.recordExecution("methodOne", 160);
        List<MethodExecutionProfile> profiles = profiler.getProfiles(descendingAverageTimeComparator);
        assertEquals(1, profiles.size());
        assertEquals("methodOne", profiles.get(0).getMethodName());
        assertEquals(new Double(140), profiles.get(0).getAverageTime());
    }

    @Test
    public void getProfilesAfterExecutionOfMultipleMethods() throws Exception {
        profiler.recordExecution("methodOne", 100);
        profiler.recordExecution("methodTwo", 200);
        List<MethodExecutionProfile> profiles = profiler.getProfiles(descendingAverageTimeComparator);
        assertEquals(2, profiles.size());
        assertEquals("methodTwo", profiles.get(0).getMethodName());
        assertEquals("methodOne", profiles.get(1).getMethodName());
    }

}
