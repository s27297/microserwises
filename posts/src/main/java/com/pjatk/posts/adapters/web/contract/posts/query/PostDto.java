package com.pjatk.posts.adapters.web.contract.posts.query;

import com.pjatk.posts.core.domain.Post;

import java.util.Date;
import java.util.List;

public record PostDto(String id, String nazwa, String opis, Date date, double averageMark) {
    public static  PostDto from(Post post) {
        List<Post.Like> likes = post.getLikes();
        double sum = 0.0;
        for (Post.Like like : likes) {
            sum += like.value();
        }
        if (sum==0)
            return new PostDto(post.getId(), post.getNazwa(), post.getOpis(), post.getDateOFCreating(),
                        sum);
        return new PostDto(post.getId(), post.getNazwa(), post.getOpis(), post.getDateOFCreating(),
                sum/likes.size());
    }

    public static Post to(Post post) {
        return new Post(post.getId(), post.getNazwa(), post.getOpis(), post.getDateOFCreating());
    }
}
