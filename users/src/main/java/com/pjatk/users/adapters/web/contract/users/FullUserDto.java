package com.pjatk.users.adapters.web.contract.users;

import com.pjatk.users.core.domain.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public record FullUserDto(String id,String name, String surname, String login,
                          Date dateOfBirth , Date dateOfJoining, String email,
                          String numerTelefonu, String type, List<SprawnoscDto>sprawnosci,
                          List<PaymentDto>payments) {
    public static FullUserDto from(User user){
        List<PaymentDto> payments = new ArrayList<>();
        if(user.getPayments() != null){
            payments=user.getPayments().stream().map(PaymentDto::from).toList();
        }
        List<SprawnoscDto> sprawnosci = new ArrayList<>();
        if(user.getSprawnosci() != null){
            sprawnosci=user.getSprawnosci().stream().map(SprawnoscDto::from).toList();
        }
        return new FullUserDto(user.getId(), user.getName(), user.getSurname(), user.getLogin(),
                user.getDateOfBirth(),user.getDateOfJoining(), user.getEmail(),
                user.getNumerTelefonu(), user.getType(),sprawnosci,payments);
    }
}
