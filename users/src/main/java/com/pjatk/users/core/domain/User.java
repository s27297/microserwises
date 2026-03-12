// src/main/java/com/pjatk/musiclab/adapters/persistance/MongoArtistDocument.java
package com.pjatk.users.core.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String name;
    private String surname;
    private String login;
    private Date dateOfBirth;
    private Date dateOfJoining;
    private String email;
    private String numerTelefonu;
    private String type;
    private List<Sprawnosc> sprawnosci=new ArrayList<Sprawnosc>();
    private List<Payment> payments=new ArrayList<Payment>();



    public record Sprawnosc(Integer source_id,String sprawnosc_type,String sprawnosc_name) {}
    public record Payment(Integer source_id,String nazwa,Date date,Double quantity) {}
    public User(){}

    public User(String name,String surname,
                String login,Date dateOfBirth,
                Date dateOfJoining,String email,
                String numerTelefonu,String type){
        this.dateOfBirth=dateOfBirth;
        this.dateOfJoining=dateOfJoining;
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.numerTelefonu = numerTelefonu;
        this.type=type;}

    public User(String name,String surname,
                String login,String email,
                String numerTelefonu,String type){
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.numerTelefonu = numerTelefonu;
        this.type=type;}
    public User(String login){this.login = login;}


    public User(String name, String surname,
                String login, Date dateOfBirth,
                Date dateOfJoining, String email,
                String numerTelefonu, String type,
                List<Sprawnosc> sprawnosci, List<Payment> payments){
//        this.dateOfBirth=new Date(dateOfBirth);
//        this.dateOfJoining=new Date(dateOfJoining);
        this.dateOfBirth=dateOfBirth;
        this.dateOfJoining=dateOfJoining;

                this.name = name;
        this.surname = surname;
        this.login = login;
        this.email = email;
        this.numerTelefonu = numerTelefonu;
        this.type=type;
        this.sprawnosci=sprawnosci;
        this.payments=payments;}
}


