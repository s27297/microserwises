package com.pjatk.posts.adapters.web.contract.posts.command;

import com.pjatk.posts.core.domain.Post;

public record UpdateImageInCommentDto(Integer comment_id, Integer source_id, String nazwa, String url) {
    public static Integer getCommentId(UpdateImageInCommentDto updateImageInCommentDto) {
        return updateImageInCommentDto.comment_id();
    }
    public static Post.Image to(UpdateImageInCommentDto updateImageInCommentDto) {
        return new Post.Image(updateImageInCommentDto.source_id, updateImageInCommentDto.nazwa(), updateImageInCommentDto.url());
    }
}
