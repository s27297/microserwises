package com.pjatk.users.adapters.persistance.mappers;

import com.mongodb.DBObject;
import com.pjatk.users.adapters.persistance.model.UserDocument;
import com.pjatk.users.core.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static UserDocument toUserDocument(User user) {
        UserDocument userDocument = new UserDocument();
        userDocument.setId(user.getId());
        userDocument.setName(user.getName());
        userDocument.setSurname(user.getSurname());
        userDocument.setLogin(user.getLogin());
        userDocument.setEmail(user.getEmail());
        userDocument.setDateOfBirth(user.getDateOfBirth());
        userDocument.setDateOfJoining(user.getDateOfJoining());
        userDocument.setNumerTelefonu(user.getNumerTelefonu());
        userDocument.setType(user.getType());
        if(user.getPayments() != null) {
            List<UserDocument.Payment> payments =
                    user.getPayments()
                            .stream()
                            .map(UserMapper::toPaymentDocument)
                            .toList();
            userDocument.setPayments(payments);
        }
        else
            userDocument.setPayments(new ArrayList<>());
        if(user.getSprawnosci() != null) {
            List<UserDocument.Sprawnosc> sprawnosci =
                    user.getSprawnosci()
                            .stream()
                            .map(UserMapper::toSprawnoscDocument)
                            .toList();
            userDocument.setSprawnosci(sprawnosci);
        }
        else
            userDocument.setSprawnosci(new ArrayList<>());

        return userDocument;
    }

    public static User toUser(UserDocument user) {
        User userDocument = new User();
        userDocument.setId(user.getId());
        userDocument.setName(user.getName());
        userDocument.setSurname(user.getSurname());
        userDocument.setLogin(user.getLogin());
        userDocument.setEmail(user.getEmail());
        userDocument.setDateOfBirth(user.getDateOfBirth());
        userDocument.setDateOfJoining(user.getDateOfJoining());
        userDocument.setNumerTelefonu(user.getNumerTelefonu());
        userDocument.setType(user.getType());
        if(user.getPayments() != null) {
            List<User.Payment> payments =
                    user.getPayments()
                            .stream()
                            .map(UserMapper::toPaymentDomain)
                            .toList();
            userDocument.setPayments(payments);
        }
        else
            userDocument.setPayments(new ArrayList<>());
        if(user.getSprawnosci() != null) {
            List<User.Sprawnosc> sprawnosci =
                    user.getSprawnosci()
                            .stream()
                            .map(UserMapper::toSprawnoscDomain)
                            .toList();
            userDocument.setSprawnosci(sprawnosci);
        }
        else
            userDocument.setSprawnosci(new ArrayList<>());

        return userDocument;
    }
    public static UserDocument.Sprawnosc toSprawnoscDocument(User.Sprawnosc sprawnosc) {
        return new UserDocument.Sprawnosc(sprawnosc.source_id(),sprawnosc.sprawnosc_type(),sprawnosc.sprawnosc_name());
    }

    public static UserDocument.Payment toPaymentDocument(User.Payment payment) {
        return new UserDocument.Payment(payment.source_id(),payment.nazwa(),payment.date(),payment.quantity());
    }

    public static User.Sprawnosc toSprawnoscDomain(UserDocument.Sprawnosc sprawnosc) {
        return new User.Sprawnosc(sprawnosc.source_id(),sprawnosc.sprawnosc_type(),sprawnosc.sprawnosc_name());
    }

    public static User.Payment toPaymentDomain(UserDocument.Payment payment) {
        return new User.Payment(payment.sorce_id(),payment.nazwa(),payment.date(),payment.quantity());
    }
}

