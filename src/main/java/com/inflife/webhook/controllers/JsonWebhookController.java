package com.inflife.webhook.controllers;

import com.inflife.webhook.common.JsonWebhookMapper;
import com.inflife.webhook.common.SuccessResponse;
import com.inflife.webhook.entities.JsonWebhook;
import com.inflife.webhook.exception.BadRequestServiceException;
import com.inflife.webhook.exception.ServiceException;
import com.inflife.webhook.services.JsonWebhookService;
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

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<List<JsonWebhook>> create(HttpEntity<String> request)
            throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(jsonWebhookService.save(request.getBody(),
                        jsonWebhookMapper::toObject));
    }

    @PostMapping("/webhook/v2")
    ResponseEntity<SuccessResponse> createV2(@RequestBody Map<String, Object> object) throws ServiceException, BadRequestServiceException {
        jsonWebhookService.saveAsync(object);
        SuccessResponse response = new SuccessResponse();
        response.setStatus("success");
        return ResponseEntity.ok(response);
    }
}
