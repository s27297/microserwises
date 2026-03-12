package com.pjatk.posts;

import com.pjatk.posts.application.*;
import com.pjatk.posts.application.port.out.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public PostsService postQueryService(PostReadPort readPort) {
        return new PostsService(readPort);
    }

    @Bean
    public PostCommandService postCommandService(PostWritePort writePort) {
        return new PostCommandService(writePort);
    }

}
