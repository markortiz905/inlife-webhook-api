package com.inflife.webhook.aspect;

import com.inflife.webhook.entities.JsonWebhook;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class JsonWebhookServiceAspect {

    @Before(value = "execution(* com.inflife.webhook.services.JsonWebhookService.saveAsync(..)) " +
            "and args(objectMap)")
    public void createAdviceBefore(JoinPoint joinPoint, Map<String, Object> objectMap) {
        log.info("saveAsync service started  - " + joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut="execution(* com.inflife.webhook.services.JsonWebhookService.saveAsync(..))",
            returning="computableObject")
    public void createAdviceAfterReturning(JoinPoint joinPoint, CompletableFuture<List<JsonWebhook>> computableObject) {
        log.info("saveAsync service ended - " + joinPoint.getSignature());
    }
}