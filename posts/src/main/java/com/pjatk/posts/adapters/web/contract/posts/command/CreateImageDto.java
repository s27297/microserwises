package com.pjatk.posts.adapters.web.contract.posts.command;


import com.pjatk.posts.core.domain.Post;

public record CreateImageDto(String nazwa, String url)  {
    public static Post.Image to(CreateImageDto dto) {
        return new Post.Image(null, dto.nazwa, dto.url);
    }
}
