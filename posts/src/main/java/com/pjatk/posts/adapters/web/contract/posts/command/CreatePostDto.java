package com.pjatk.posts.adapters.web.contract.posts.command;

import com.pjatk.posts.core.domain.Post;

import java.util.ArrayList;
import java.util.Date;

public record CreatePostDto(String nazwa, String opis) {
    public static Post to(CreatePostDto dto) {
        return new Post(null, dto.nazwa(), dto.opis(),
                new Date(),new ArrayList<>(),new ArrayList<>(), new ArrayList<>());
    }
}
