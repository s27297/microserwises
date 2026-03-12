package com.pjatk.posts.adapters.web.contract.posts.command;

import com.pjatk.posts.core.domain.Post;

public record CreateImageToCommentDto(Integer comment_id, String nazwa, String url) {
    public static Integer getCommentId(CreateImageToCommentDto createImageToCommentDto) {
        return createImageToCommentDto.comment_id();
    }
    public static Post.Image to(CreateImageToCommentDto createImageToCommentDto) {
        return new Post.Image(null, createImageToCommentDto.nazwa(), createImageToCommentDto.url());
    }
}
