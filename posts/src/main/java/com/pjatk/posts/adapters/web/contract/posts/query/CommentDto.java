package com.pjatk.posts.adapters.web.contract.posts.query;

import com.pjatk.posts.core.domain.Post;

import java.util.List;

public record CommentDto(Integer source_id, String user_id, String text,
                         List<ImageDto> images, String reply)  {
    public static CommentDto from(Post.Comment comment) {
        return new CommentDto(comment.source_id(), comment.user_id(), comment.text(),
                comment.images().stream().map(ImageDto::from).toList(), comment.reply());
    }

    public static Post.Comment to(CommentDto comment) {
        return new Post.Comment(comment.source_id(), comment.user_id(), comment.text(),
                comment.images().stream().map(ImageDto::to).toList(), comment.reply());
    }

}
