package com.daugherty.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.daugherty.profile.MethodExecutionProfileRecorder;

/**
 * Interceptor that records the time taken to execute the intercepted method.
 */
@Aspect
public class ProfilingInterceptor {

    private MethodExecutionProfileRecorder profileRecorder;

    public void setProfileRecorder(MethodExecutionProfileRecorder profileRecorder) {
        this.profileRecorder = profileRecorder;
    }

    @Around("execution(* com.daugherty.e2c..*.*(..))")
    public Object doProfiling(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed(joinPoint.getArgs());
        } catch (Throwable e) {
            throw e;
        } finally {
            long elapsedTime = System.currentTimeMillis() - startTime;
            profileRecorder.recordExecution(buildFullyQualifiedMethodName(joinPoint), elapsedTime);
        }
    }

    private String buildFullyQualifiedMethodName(ProceedingJoinPoint joinPoint) {
        return joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
    }

}
