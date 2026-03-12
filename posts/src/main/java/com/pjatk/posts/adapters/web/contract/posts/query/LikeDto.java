package com.pjatk.posts.adapters.web.contract.posts.query;
import com.pjatk.posts.core.domain.Post;

public record LikeDto(Integer source_id,String user_id,int value)  {
    public static LikeDto from(Post.Like like) {
        return new LikeDto(like.source_id(),like.user_id(),like.value());
    }
    public static Post.Like to(LikeDto dto) {
        return new Post.Like(dto.source_id(),dto.user_id(),dto.value());
    }

}
