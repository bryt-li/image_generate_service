package co.redin.image_generate_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class ActionLogAspect {
    @Before("execution(* com.orienteexpress.lollipop.controller.api..*.*(..)) " +
            "|| execution(* com.orienteexpress.lollipop.controller.proxy..*.*(..))")
    public void logBeforeAllMethods(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        log.info("action request: uri=[{}] method=[{}]\nargs={}",
                request.getRequestURI(), request.getMethod(), Arrays.toString(args));
    }

    @AfterReturning(pointcut = "execution(* com.orienteexpress.lollipop.controller..*.*(..)))", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        ResponseEntity<?> response = (ResponseEntity<?>) result;
        log.info("action response: http_code=[{}]\nhttp_body=[{}]", response.getStatusCode(), response.getBody());
    }
}
