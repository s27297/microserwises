package com.pjatk.posts.adapters.persistance.mappers;

import com.pjatk.posts.adapters.persistance.model.PostDocument;
import com.pjatk.posts.core.domain.Post;

public class PostMapper {
    public static Post.Like toLikeDomain (PostDocument.Like like){
        if(like == null)return null;
        return new Post.Like(like.source_id(),like.user_id(), like.value());
    }

    public static PostDocument.Like toLikeDocument (Post.Like like){
        if (like == null)return null;
        return new PostDocument.Like(like.source_id(),like.user_id(), like.value());
    }
    public static Post.Image toImageDomain (PostDocument.Image image){
        if(image == null) return null;
        return new Post.Image(image.source_id(),image.nazwa(),image.url());
    }

    public static PostDocument.Image toImageDocument (Post.Image image){
        if(image == null) return null;
        return new PostDocument.Image(image.source_id(),image.nazwa(),image.url());
    }

    public static Post.Comment toCommentDomain (PostDocument.Comment comment){
        if(comment == null) return null;
        return new Post.Comment(comment.source_id(),comment.user_id(),comment.text(),
                comment.images().stream().map(PostMapper::toImageDomain).toList(),comment.reply());
    }

    public static PostDocument.Comment toCommentDocument (Post.Comment comment){
        if(comment == null) return null;
        return new PostDocument.Comment(comment.source_id(),comment.user_id(),comment.text(),
                comment.images().stream().map(PostMapper::toImageDocument).toList(),comment.reply());
    }

    public static Post toPostDomain (PostDocument post){
        if(post== null) return null;
        return new Post(post.getId(), post.getNazwa(), post.getOpis(),post.getDateOFCreating(),
                post.getComments().stream().map(PostMapper::toCommentDomain).toList(),
                post.getImages().stream().map(PostMapper::toImageDomain).toList(),
                post.getLikes().stream().map(PostMapper::toLikeDomain).toList());
    }


    public static PostDocument toPostDocument (Post post){
        if(post== null) return null;
        return new PostDocument(post.getId(), post.getNazwa(), post.getOpis(),post.getDateOFCreating(),
                post.getComments().stream().map(PostMapper::toCommentDocument).toList(),
                post.getImages().stream().map(PostMapper::toImageDocument).toList(),
                post.getLikes().stream().map(PostMapper::toLikeDocument).toList());
    }


}
