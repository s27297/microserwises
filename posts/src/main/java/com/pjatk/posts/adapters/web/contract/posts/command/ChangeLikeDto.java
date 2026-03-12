package com.pjatk.posts.adapters.web.contract.posts.command;

import com.pjatk.posts.core.domain.Post;

public record ChangeLikeDto(String user_id, int value) {
    public static Post.Like to(ChangeLikeDto likeDto){
        return new Post.Like(null, likeDto.user_id(), likeDto.value());
    }
}
