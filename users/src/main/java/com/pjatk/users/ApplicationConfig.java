package com.pjatk.users;

import com.pjatk.users.application.*;
import com.pjatk.users.core.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {


    @Bean
    public UsersQueryService userQueryService(UsersReadPort readPort) {
        return new UsersQueryService(readPort);
    }

    @Bean
    public UserCommandService userCommandService(UserWritePort writePort) {
        return new UserCommandService(writePort);
    }
}
