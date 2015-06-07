package com.daugherty.aop;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.daugherty.profile.MethodExecutionProfileRecorder;

@RunWith(MockitoJUnitRunner.class)
public class ProfilingInterceptorTest {

    private ProfilingInterceptor interceptor;
    @Mock
    private MethodExecutionProfileRecorder profileRecorder;
    @Mock
    private ProceedingJoinPoint joinPoint;
    @Mock
    private Signature signature;

    @Before
    public void setUpFixtures() {
        interceptor = new ProfilingInterceptor();
        interceptor.setProfileRecorder(profileRecorder);

        when(signature.getDeclaringTypeName()).thenReturn("QueryCriteria");
        when(signature.getName()).thenReturn("getLanguage");
        when(joinPoint.getSignature()).thenReturn(signature);
    }

    @Test
    public void doProfilingForMethodWithoutExceptionRecordsExecution() throws Throwable {
        when(joinPoint.proceed(Mockito.any(Object[].class))).thenReturn("returnValue");

        Object returnValue = interceptor.doProfiling(joinPoint);

        assertThat(returnValue.toString(), is("returnValue"));
        verify(profileRecorder).recordExecution(Mockito.eq("QueryCriteria.getLanguage"), Mockito.anyLong());
    }

    @Test
    public void doProfilingForMethodWithExceptionRecordsExecution() throws Throwable {
        when(joinPoint.proceed(Mockito.any(Object[].class))).thenThrow(
                new RuntimeException("An error occurred in the target method"));

        try {
            interceptor.doProfiling(joinPoint);
            fail("Should throw a RuntimeException");
        } catch (RuntimeException e) {
            verify(profileRecorder).recordExecution(Mockito.eq("QueryCriteria.getLanguage"), Mockito.anyLong());
        }
    }

}
