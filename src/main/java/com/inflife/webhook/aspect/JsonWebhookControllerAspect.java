package com.inflife.webhook.aspect;

import com.inflife.webhook.common.SuccessResponse;
import com.inflife.webhook.entities.JsonWebhook;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Aspect
@Component
public class JsonWebhookControllerAspect {


    @Before(value = "execution(* com.inflife.webhook.controllers.JsonWebhookController.createV2(..)) " +
            "and args(request)")
    public void createAdviceBefore(JoinPoint joinPoint, HttpEntity<String> request) {
        log.info("Inbound Request Started  - " + joinPoint.getSignature());
    }

    @AfterReturning(
            pointcut="execution(* com.inflife.webhook.controllers.JsonWebhookController.createV2(..))",
            returning="jsonWebhook")
    public void createAdviceAfterReturning(ResponseEntity<SuccessResponse> jsonWebhook) {
        log.info("Returning JsonWebhook - status code:" + jsonWebhook.getStatusCodeValue());
    }
}