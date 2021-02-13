package com.inlife.webhook.repositories.jpa;

import com.inlife.webhook.entities.JsonWebhook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * @author mark ortiz
 */
public interface JsonWebhookRepository extends CrudRepository<JsonWebhook, Long> {

    @Modifying
    @Query("UPDATE JsonWebhook u " +
            "SET u.jsonString = JSON_MERGE_PATCH(:jsonDoc, :jsonEdit) " +
            "WHERE u.id = :id")
    JsonWebhook update(Long id, String jsonDoc, String jsonEdit);

    //List<JsonWebhook> saveAll(List<JsonWebhook> entities);
}
