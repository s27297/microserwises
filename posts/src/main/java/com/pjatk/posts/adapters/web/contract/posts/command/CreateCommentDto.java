package com.pjatk.posts.adapters.web.contract.posts.command;

import com.pjatk.posts.core.domain.Post;

import java.util.ArrayList;

public record CreateCommentDto(String user_id, String text,
                                String reply) {
    public static Post.Comment toComment(CreateCommentDto createCommentDto) {
        return new Post.Comment(null, createCommentDto.user_id(),
                createCommentDto.text(),new ArrayList<>(), createCommentDto.reply() );
    }
}
