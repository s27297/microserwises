// src/main/java/com/pjatk/musiclab/adapters/persistance/MongoArtistDocument.java
package com.pjatk.users.adapters.persistance.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("users")
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String surname;
    private String login;
    @Field("date_of_birth") private Date dateOfBirth;
    @Field("date_of_joining") private Date dateOfJoining;
    private String email;
    @Field("numer_telefonu") private String numerTelefonu;
    private String type;



    private List<Sprawnosc> sprawnosci;
    private List<Payment> payments;

    public record Sprawnosc(Integer source_id,String sprawnosc_type,String sprawnosc_name) {}
    public record Payment(Integer sorce_id,String nazwa,Date date,Double quantity) {}

}


