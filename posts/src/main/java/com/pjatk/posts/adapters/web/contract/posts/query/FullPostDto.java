package com.pjatk.posts.adapters.web.contract.posts.query;

import com.pjatk.posts.core.domain.Post;

import java.util.Date;
import java.util.List;

public record FullPostDto(String id, String nazwa, String opis, Date date,
                          List<LikeDto> likes, List<CommentDto> comments,
                          List<ImageDto> images) {
    public static FullPostDto from(Post post) {
        return new FullPostDto(post.getId(), post.getNazwa(), post.getOpis(),
                post.getDateOFCreating(),post.getLikes().stream().map(LikeDto::from).toList(),
                post.getComments().stream().map(CommentDto::from).toList(),
                post.getImages().stream().map(ImageDto::from).toList());
    }

    public static Post to(FullPostDto post) {
        return new Post(post.id(), post.nazwa(), post.opis(),
                post.date(), post.comments().stream().map(CommentDto::to).toList(),
                post.images().stream().map(ImageDto::to).toList(),
                post.likes().stream().map(LikeDto::to).toList());
    }
}
