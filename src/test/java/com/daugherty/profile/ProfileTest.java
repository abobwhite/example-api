package com.daugherty.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ProfileTest {

    private static final double TOLERANCE_DECIMAL_PLACES = 0.01;

    @Test
    public void getTotalTimeWithoutExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        assertNull("No total time should exist if method has never been called", profile.getTotalTime());
    }

    @Test
    public void getMinimumTimeWithoutExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        assertNull("No minimum time should exist if method has never been called", profile.getMinimumTime());
    }

    @Test
    public void getMaximumTimeWithoutExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        assertNull("No maximum time should exist if method has never been called", profile.getMaximumTime());
    }

    @Test
    public void getAverageTimeWithoutExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        assertNull("No average time should exist if method has never been called", profile.getAverageTime());
    }

    @Test
    public void getStandardDeviationWithoutExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        assertNull("No standard deviation should exist if method has never been called", profile.getStandardDeviation());
    }

    @Test
    public void getTotalTimeAfterExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        assertEquals(new Long(100), profile.getTotalTime());
        profile.recordElapsedTime(120);
        assertEquals(new Long(220), profile.getTotalTime());
        profile.recordElapsedTime(140);
        assertEquals(new Long(360), profile.getTotalTime());
        profile.recordElapsedTime(160);
        assertEquals(new Long(520), profile.getTotalTime());
        profile.recordElapsedTime(180);
        assertEquals(new Long(700), profile.getTotalTime());
    }

    @Test
    public void getMinimumTimeAfterExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        assertEquals(new Long(100), profile.getMinimumTime());
        profile.recordElapsedTime(120);
        assertEquals(new Long(100), profile.getMinimumTime());
        profile.recordElapsedTime(140);
        assertEquals(new Long(100), profile.getMinimumTime());
        profile.recordElapsedTime(160);
        assertEquals(new Long(100), profile.getMinimumTime());
        profile.recordElapsedTime(180);
        assertEquals(new Long(100), profile.getMinimumTime());
    }

    @Test
    public void getMaximumTimeAfterExecutions() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        assertEquals(new Long(100), profile.getMaximumTime());
        profile.recordElapsedTime(120);
        assertEquals(new Long(120), profile.getMaximumTime());
        profile.recordElapsedTime(140);
        assertEquals(new Long(140), profile.getMaximumTime());
        profile.recordElapsedTime(160);
        assertEquals(new Long(160), profile.getMaximumTime());
        profile.recordElapsedTime(180);
        assertEquals(new Long(180), profile.getMaximumTime());
    }

    @Test
    public void getAverageTimeAfterExecutionsWithinMaximum() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        assertEquals(new Double(100), profile.getAverageTime());
        profile.recordElapsedTime(120);
        assertEquals(new Double(110), profile.getAverageTime());
        profile.recordElapsedTime(140);
        assertEquals(new Double(120), profile.getAverageTime());
    }

    @Test
    public void getAverageTimeAfterExecutionsOverMaximum() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        profile.recordElapsedTime(120);
        profile.recordElapsedTime(140);
        profile.recordElapsedTime(160);
        assertEquals(new Double(140), profile.getAverageTime());
        profile.recordElapsedTime(180);
        assertEquals(new Double(160), profile.getAverageTime());
    }

    @Test
    public void getStandardDeviationAfterExecutionsWithinMaximum() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        assertEquals(new Double(0), profile.getStandardDeviation());
        profile.recordElapsedTime(120);
        assertEquals(new Double(14.14), profile.getStandardDeviation(), TOLERANCE_DECIMAL_PLACES);
        profile.recordElapsedTime(140);
        assertEquals(new Double(20), profile.getStandardDeviation(), TOLERANCE_DECIMAL_PLACES);
    }

    @Test
    public void getStandardDeviationAfterExecutionsOverMaximum() throws Exception {
        MethodExecutionProfile profile = new MethodExecutionProfile("method", 3);
        profile.recordElapsedTime(100);
        profile.recordElapsedTime(120);
        profile.recordElapsedTime(150);
        profile.recordElapsedTime(190);
        assertEquals(new Double(35.11), profile.getStandardDeviation(), TOLERANCE_DECIMAL_PLACES);
        profile.recordElapsedTime(240);
        assertEquals(new Double(45.09), profile.getStandardDeviation(), TOLERANCE_DECIMAL_PLACES);
    }

}
