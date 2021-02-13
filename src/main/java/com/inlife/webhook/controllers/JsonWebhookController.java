package com.inlife.webhook.controllers;

import com.inlife.webhook.common.JsonWebhookMapper;
import com.inlife.webhook.common.SuccessResponse;
import com.inlife.webhook.entities.JsonWebhook;
import com.inlife.webhook.exception.BadRequestServiceException;
import com.inlife.webhook.exception.ServiceException;
import com.inlife.webhook.services.JsonWebhookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author mark ortiz
 */
@Slf4j
@RestController
public class JsonWebhookController {

    @Autowired
    private JsonWebhookService jsonWebhookService;
    @Autowired
    private JsonWebhookMapper jsonWebhookMapper;

    @PostMapping("/webhook/bulk")
    ResponseEntity<SuccessResponse> createBulk(@RequestBody Map<String, Object> object) throws ServiceException, BadRequestServiceException {
        jsonWebhookService.saveAsync(object);
        SuccessResponse response = new SuccessResponse();
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    ResponseEntity<SuccessResponse> createEntry(@RequestBody Map<String, Object> object) throws ServiceException, BadRequestServiceException {
        jsonWebhookService.saveEntry(object);
        SuccessResponse response = new SuccessResponse();
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }
}
