package com.inflife.webhook.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inflife.webhook.entities.JsonWebhook;
import com.inflife.webhook.exception.BadRequestServiceException;
import com.inflife.webhook.exception.ServiceException;
import com.inflife.webhook.repositories.jpa.JsonWebhookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Primary
@Service
public class JsonWebhookServiceImpl implements JsonWebhookService {

    @Autowired
    private JsonWebhookRepository jsonWebhookRepository;

    @Autowired
    private ObjectMapper mapper;

    @Override
    public List<JsonWebhook> save(String payload, Function<String, List<JsonWebhook>> toObject) throws ServiceException, BadRequestServiceException {
        return StreamSupport.stream(jsonWebhookRepository.saveAll(toObject.apply(payload)).spliterator(), false)
                        .collect(Collectors.toList());
    }

    @Override
    @Async
    public CompletableFuture<Void> saveAsync(Map<String, Object> object) throws ServiceException, BadRequestServiceException {
        List<Object> objects = (List<Object>)object.get("items");
        return CompletableFuture.supplyAsync(() -> objects.parallelStream().map(obj -> {
            JsonWebhook hooks = new JsonWebhook();
            Long id = Long.valueOf(((Integer)((Map<String, Object>)obj).get("item_id")).toString());
            boolean exist = jsonWebhookRepository.existsById(id);
            //log.info("id " + id + " found? " + exist);
            if (!exist) {
                hooks.setDateCreated(LocalDate.now());
            }
            hooks.setDateUpdated(LocalDate.now());
            hooks.setId(id);
            List<Object> fields = (List<Object>)((Map<String, Object>) obj).get("fields");
            fields.stream().forEach( f -> {
                try {
                    Map<String, Object> field = (Map<String, Object>) f;
                    if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("your name")) {
                        hooks.setFullName((String) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("primary inlife contact")) {
                        hooks.setPrimaryInlifeContact((String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value")).get("title"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("date opened")) {
                        hooks.setDateOpened(LocalDate.parse(
                                (String) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("start_date"),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("nature of disability")) {
                        hooks.setNatureOfDisability((String) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("please describe the supports required ")) {
                        hooks.setSupportsRequiredDescription((String) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).contains("suburb")) {
                        hooks.setSuburb((String) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).equalsIgnoreCase("expected time to onboard")) {
                        hooks.setExpectedTimeToOnboard((String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value")).get("text"));
                    } else if (((String) field.get("label")).toLowerCase(Locale.ROOT).equalsIgnoreCase("status")) {
                        hooks.setStatus((String) ((Map<String, Object>) ((Map<String, Object>) ((List<Object>) field.get("values")).get(0)).get("value")).get("text"));
                    }
                } catch (Exception e) {}
            });
            try {
                hooks.setJsonString(mapper.writeValueAsString(obj));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return hooks;
        }).collect(Collectors.toList()))
                .thenAccept(result -> {
                    log.info("saving all " + result.size() + " entries to db..");
                    jsonWebhookRepository.saveAll(result);
                })
                .exceptionally( e -> { e.printStackTrace(); return null;})
                .thenRun(() -> log.info("finished saving all jsonwebhooks"));
    }
}
