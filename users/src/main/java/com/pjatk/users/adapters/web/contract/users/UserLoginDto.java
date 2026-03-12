package com.pjatk.users.adapters.web.contract.users;

import com.pjatk.users.core.domain.User;

public record UserLoginDto(String login){
    public static UserLoginDto from(User user){return new UserLoginDto(user.getLogin());}
}
