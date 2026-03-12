package com.pjatk.posts.adapters.web.contract.posts.command;



import com.pjatk.posts.core.domain.Post;

import java.util.ArrayList;

public record UpdateCommentDto(Integer source_id, String user_id, String text,
                               String reply) {
    public static Post.Comment to(UpdateCommentDto comment) {
        return new Post.Comment(comment.source_id(), comment.user_id(), comment.text(),
                new ArrayList<>(), comment.reply());
    }
}
