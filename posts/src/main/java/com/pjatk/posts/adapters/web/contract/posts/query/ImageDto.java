package com.pjatk.posts.adapters.web.contract.posts.query;

import com.pjatk.posts.core.domain.Post;

public record ImageDto(Integer source_id, String nazwa, String url)  {
    public static ImageDto from(Post.Image image) {
        return new ImageDto(image.source_id(), image.nazwa(), image.url());
    }
    public static Post.Image to(ImageDto imageDto) {
        return new Post.Image(imageDto.source_id(), imageDto.nazwa(), imageDto.url);
    }
}
