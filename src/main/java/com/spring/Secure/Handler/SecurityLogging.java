package com.spring.Secure.Handler;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SecurityLogging {
    @Before("execution(* com.spring.Secure.*.*(..))")
    public void logBefore(JoinPoint joinPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null){
            String username = authentication.getName();
            Object password = authentication.getCredentials();
            log.info("* Trying to log in username:{}",username);
            log.info("* password:{}",password==null?"null":password);
        }
    }
    @AfterReturning("execution(* com.spring.Secure.*.*(..))")
    public void logAfter(JoinPoint joinPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null) {


            log.info("* Login successful username:{}",
                    authentication.getName());
        }
    }
    @AfterThrowing("execution(* com.spring.Secure.*.*(..))")
    public void logAfterThrowing(JoinPoint joinPoint){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication!=null) {
            log.info("* Login failed username:{}", SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }
}
