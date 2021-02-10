package com.inflife.webhook.entities;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "json_webhook")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JsonWebhook {

    @Id
    @EqualsAndHashCode.Include
    private Long id; //item_id

    private String fullName;
    private String primaryInlifeContact;
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOpened;
    private String status;
    private String natureOfDisability;
    @Column(name="your_column_name",columnDefinition="LONGTEXT")
    private String supportsRequiredDescription;
    private String suburb;
    private String expectedTimeToOnboard;


    @Column(name = "jsonObject", columnDefinition = "json")
    @JsonRawValue
    @JsonIgnore
    private String jsonString;

    @Override
    public String toString() {
        return "JsonWebhook{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", primaryInlifeContact='" + primaryInlifeContact + '\'' +
                ", dateOpened=" + dateOpened +
                ", status='" + status + '\'' +
                ", natureOfDisability='" + natureOfDisability + '\'' +
                ", supportsRequiredDescription='" + supportsRequiredDescription + '\'' +
                ", suburb='" + suburb + '\'' +
                ", expectedTimeToOnboard='" + expectedTimeToOnboard + '\'' +
                '}';
    }
}