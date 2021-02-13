package com.inlife.webhook.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inlife.webhook.entities.JsonWebhook;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.mysql.cj.xdevapi.JsonArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class JsonWebhookMapper {
    @Autowired private ObjectMapper mapper;
    public List<JsonWebhook> toObject(String json) {
        return JsonPath.<List<Object>>read(Configuration.defaultConfiguration().jsonProvider().parse(json),"$.items[*]")
                .stream()
                .map(this::toHashMap)
                .map(this::toObject)
                .collect(Collectors.toList()).get(0);
    }

    private List<JsonWebhook> toObject(Map<Object, Map<String,  Optional<Object>>> map) {
        return map.entrySet().stream()
                .map( e -> {

                    Map<String,  Optional<Object>> fields = e.getValue();
                    log.info("json -> " + JsonPath.<JsonArray>read(fields.get("your name").get(), "$..value").get(0).toString());
                    try {
                        log.info(mapper.writeValueAsString(e.getKey()));
                        return JsonWebhook.builder()
                                .id(Long.valueOf(JsonPath.<Integer>read(e.getKey(), "$.item_id")))
                                .fullName(JsonPath.<String>read(fields.get("your name").orElse(""), "$..value"))
                                .primaryInlifeContact(JsonPath.<String>read(fields.get("primary inlife contact").orElse(""), "$..value.title"))
                                .dateOpened(LocalDate.parse(JsonPath.<String>read(fields.get("date opened").orElse(""), "$..start_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"))) //2020-11-13
                                .status(JsonPath.<String>read(fields.get("status").orElse(""), "$..text"))
                                .natureOfDisability(JsonPath.<String>read(fields.get("nature of disability").orElse(""), "$..value"))
                                .supportsRequiredDescription(JsonPath.<String>read(fields.entrySet().stream().filter((p) -> p.getKey().contains("Please describe the supports required")).findFirst().get().getValue().orElse(""), "$..value"))
                                .suburb(JsonPath.<String>read(fields.get("suburb").orElse(""), "$..value"))
                                .expectedTimeToOnboard(JsonPath.<String>read(fields.get("expected time to onboard").orElse(""), "$..value.text"))
                                .jsonString(mapper.writeValueAsString(e.getKey()))
                                .build();
                    } catch (Exception ex) {log.error(ex.getMessage(), ex);}
                    return null;

                }).onClose(() -> log.info("finished transforming to list of jsonwebhook")).collect(Collectors.toList());
    }

    private Map<Object, Map<String, Optional<Object>>> toHashMap(Object item) {
        return new HashMap<>() {
            {
                put(item, JsonPath.<List<Object>>read(item, "$.fields[*]")
                        .stream().filter(JsonWebhookMapper::filter)
                        .collect(Collectors.toMap(
                                e -> JsonPath.<String>read(e, "$.label").toLowerCase(Locale.ROOT),
                                e -> Optional.ofNullable(JsonPath.<Object>read(e, "$.values"))
                        )));
            }
        };
    }

    private static boolean filter(Object obj) {
        return Stream.<String>of("Your name","Primary InLife contact","Date opened",
                "Status", "Nature of disability", "Please describe the supports required (eg days/times, activities required)",
                "Suburb", "Expected time to onboard")
                .anyMatch((str) -> str.equalsIgnoreCase(JsonPath.<String>read(obj, "$.label")) );
    }
}
