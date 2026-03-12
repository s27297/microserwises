package com.pjatk.posts.application.port.out;

import com.pjatk.posts.core.domain.Post;


public interface PostWritePort {
    String save(Post post);
    Post update(String id, Post post);
    boolean deleteById(String id);
    String changeLike(String id, Post.Like like);
    Integer addImage(String id,Post.Image image);
    String deleteImage(String id,int source_id);
    Integer addComment(String id,Post.Comment comment);
    String deleteComment(String id,int source_id);
    Integer addImageToComment(String id,int source_id,Post.Image image);
    String deleteImageToComment(String id,int comment_id,int source_id);
    String updateImage(String id, int imageId, Post.Image newImage) ;
    String updateComment(String id, int comment_id, Post.Comment newComment);
    String updateImageInComment(String id,int commentId, int imageId, Post.Image newImage) ;
}
