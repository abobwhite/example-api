package com.daugherty.aop;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interceptor that provides fine-level logging for all non-domain, non-aspect E2C classes, if enabled.
 */
@Aspect
public class LoggingInterceptor {
    private static final String INDENT_TEXT = "..";

    private int depth;

    private List<String> excludedClassNames = new ArrayList<String>();

    public void setExcludedClassNames(List<String> excludedClassNames) {
        if (excludedClassNames != null) {
            this.excludedClassNames = excludedClassNames;
        }
    }

    @Around("execution(* com.daugherty..*.*(..)) && !(execution(* com.daugherty..domain.*.*(..)))"
            + " && !(execution(* com.daugherty..aop.*.*(..))) && !(execution(* com.daugherty..profile.*.*(..)))")
    public Object doLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<? extends Object> targetClass = joinPoint.getTarget().getClass();
        Logger targetLog = LoggerFactory.getLogger(targetClass);
        String methodText = MessageFormat.format("{0} in {1}", joinPoint.toShortString(), targetClass);
        String methodAndArgumentListText = MessageFormat.format("{0} with arguments {1}", methodText,
                ArrayUtils.toString(joinPoint.getArgs()));

        logMethodStart(targetLog, methodText, methodAndArgumentListText);
        try {
            Object returnValue = joinPoint.proceed(joinPoint.getArgs());
            logMethodEnd(targetLog, returnValue, methodAndArgumentListText);
            return returnValue;
        } catch (Throwable throwable) {
            logException(targetLog, throwable, methodText, methodAndArgumentListText);
            throw throwable;
        }
    }

    private void logMethodStart(Logger log, String methodText, String methodAndArgumentListText) {
        if (isLoggableTopLevelMethod(log) && classIsNotExcluded(methodText)) {
            log.info(MessageFormat.format("Starting top-level method {0}", methodText));
        }
        if (canTrace(log)) {
            log.trace(MessageFormat.format("{0}Starting method {1}", indentStart(), methodAndArgumentListText));
        } else {
            depth++;
        }
    }

    private boolean classIsNotExcluded(String methodText) {
        String currentMethodClassName = StringUtils.substringAfterLast(methodText, "class ");
        return !excludedClassNames.contains(currentMethodClassName);
    }

    private void logMethodEnd(Logger log, Object returnValue, String methodAndArgumentListText) {
        if (canTrace(log)) {
            log.trace(MessageFormat.format("{0}Ending method {1}, which returned {2}", indentEnd(),
                    methodAndArgumentListText, returnValue));
        } else {
            --depth;
        }
    }

    private void logException(Logger log, Throwable throwable, String methodText, String methodAndArgumentListText) {
        if (canTrace(log)) {
            log.trace(MessageFormat.format("{0}Ending method {1}, which threw {2}", indentEnd(),
                    methodAndArgumentListText, throwable));
        } else {
            --depth;
        }
        if (isLoggableTopLevelMethod(log)) {
            log.error(MessageFormat.format("Top-level method {0} threw {1}", methodText, throwable));
        }
    }

    private String indentStart() {
        return StringUtils.repeat(INDENT_TEXT, depth++);
    }

    private String indentEnd() {
        return StringUtils.repeat(INDENT_TEXT, --depth);
    }

    private boolean canTrace(Logger log) {
        return log != null && log.isTraceEnabled();
    }

    private boolean isLoggableTopLevelMethod(Logger log) {
        return log != null && depth == 0;
    }

}
