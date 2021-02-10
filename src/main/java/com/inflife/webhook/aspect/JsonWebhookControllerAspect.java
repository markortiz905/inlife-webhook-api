package com.inflife.webhook.aspect;

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


    @Before(value = "execution(* com.inflife.webhook.controllers.JsonWebhookController.create(..)) " +
            "and args(request)")
    public void createAdviceBefore(JoinPoint joinPoint, HttpEntity<String> request) {
        log.info("Inbound Request Started  - " + joinPoint.getSignature());
        //log.info("request payload: " + request.getBody());
    }

    @AfterReturning(
            pointcut="execution(* com.inflife.webhook.controllers.JsonWebhookController.create(..))",
            returning="jsonWebhook")
    public void createAdviceAfterReturning(ResponseEntity<List<JsonWebhook>> jsonWebhook) {
        log.info("Returning JsonWebhook - status code:" + jsonWebhook.getStatusCodeValue());
    }
}