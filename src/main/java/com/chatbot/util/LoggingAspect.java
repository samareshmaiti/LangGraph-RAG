package com.chatbot.util;



import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final String TRACE_ID = "traceId";

    @Around("""
            execution(public * com.chatbot.controller..*(..)) ||
            execution(public * com.chatbot.service..*(..)) ||
            execution(public * com.chatbot.ai.node..*(..)) ||
            execution(public * com.chatbot.ai.graph..*(..)) ||
            execution(public * com.chatbot.ai.state..*(..)) ||
            execution(public * com.chatbot.repository..*(..)) ||
            execution(public * com.chatbot.mapper..*(..)) ||
            execution(public * com.chatbot.util..*(..)) ||
            execution(public * com.chatbot.security..*(..))
            """)
    public Object logExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        boolean createdTrace = false;

        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, UUID.randomUUID().toString());
            createdTrace = true;
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        long start = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug(
                    "ENTER {}.{}({})",
                    className,
                    methodName,
                    formatArguments(joinPoint.getArgs())
            );
        } else {
            log.info("ENTER {}.{}()", className, methodName);
        }

        try {

            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - start;

            log.info(
                    "EXIT {}.{}() [{} ms]",
                    className,
                    methodName,
                    executionTime
            );

            return result;

        } catch (Exception ex) {

            long executionTime =
                    System.currentTimeMillis() - start;

            log.error(
                    "ERROR {}.{}() [{} ms] : {}",
                    className,
                    methodName,
                    executionTime,
                    ex.getMessage(),
                    ex
            );

            throw ex;

        } finally {

            if (createdTrace) {
                MDC.remove(TRACE_ID);
            }
        }
    }

    private String formatArguments(Object[] args) {

        return Arrays.stream(args)
                .map(this::safeValue)
                .collect(Collectors.joining(", "));
    }

    private String safeValue(Object arg) {

        if (arg == null) {
            return "null";
        }

        String className =
                arg.getClass().getSimpleName();

        String value =
                String.valueOf(arg);

        value = value.replaceAll("(?i)Bearer\\s+[A-Za-z0-9._-]+", "Bearer ****");
        value = value.replaceAll("(?i)password=[^,}\\]]+", "password=****");
        value = value.replaceAll("(?i)token=[^,}\\]]+", "token=****");
        value = value.replaceAll("(?i)secret=[^,}\\]]+", "secret=****");

        if (value.length() > 300) {
            value = value.substring(0, 300) + "...";
        }

        return className + "=" + value;
    }
}

