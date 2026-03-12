package com.pjatk.users.adapters.web.contract.users;

import com.pjatk.users.core.domain.User;

import java.util.Date;

public record UserDto(String id,String name, String surname, String login,
                      Date dateOfBirth ,Date dateOfJoining, String email,
                      String numerTelefonu,String type) {
    public static UserDto from(User user){
        return new UserDto(user.getId(),user.getName(), user.getSurname(), user.getLogin(),
                user.getDateOfBirth(),user.getDateOfJoining(), user.getEmail(),
                user.getNumerTelefonu(), user.getType());
    }

    public static User to(UserDto user){
        return new User(user.name(), user.surname(), user.login(),
                user.dateOfBirth(),new Date(), user.email(),
                user.numerTelefonu(), user.type());
    }
}
