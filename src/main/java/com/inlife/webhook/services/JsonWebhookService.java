package com.inlife.webhook.services;

import com.inlife.webhook.entities.JsonWebhook;
import com.inlife.webhook.exception.BadRequestServiceException;
import com.inlife.webhook.exception.ServiceException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface JsonWebhookService {

    public CompletableFuture<Void> saveEntry(Map<String, Object> object) throws ServiceException, BadRequestServiceException;

    public CompletableFuture<Void> saveAsync(Map<String, Object> object) throws ServiceException, BadRequestServiceException;
    public List<JsonWebhook> save(String payload, Function<String, List<JsonWebhook>> webhooks) throws ServiceException, BadRequestServiceException;
}
