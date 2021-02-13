package com.inlife.webhook.aspect;

import com.inlife.webhook.common.SuccessResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class JsonWebhookControllerAspect {


    @Before(value = "execution(* com.inlife.webhook.controllers.JsonWebhookController.createBulk(..)) " +
            "and args(request)")
    public void createAdviceBefore(JoinPoint joinPoint, HttpEntity<String> request) {
        log.info("Inbound Request Started  - " + joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut="execution(* com.inlife.webhook.controllers.JsonWebhookController.createBulk(..))",
            returning="jsonWebhook")
    public void createAdviceAfterReturning(ResponseEntity<SuccessResponse> jsonWebhook) {
        log.info("Returning JsonWebhook - status code:" + jsonWebhook.getStatusCodeValue());
    }

    @Before(value = "execution(* com.inlife.webhook.controllers.JsonWebhookController.createEntry(..)) " +
            "and args(request)")
    public void createEntryAdviceBefore(JoinPoint joinPoint, HttpEntity<String> request) {
        log.info("Inbound Request Started  - " + joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut="execution(* com.inlife.webhook.controllers.JsonWebhookController.createEntry(..))",
            returning="jsonWebhook")
    public void createEntryAdviceAfterReturning(ResponseEntity<SuccessResponse> jsonWebhook) {
        log.info("Returning JsonWebhook - status code:" + jsonWebhook.getStatusCodeValue());
    }
}