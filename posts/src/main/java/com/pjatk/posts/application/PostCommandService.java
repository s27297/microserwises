package com.pjatk.posts.application;

import com.pjatk.posts.application.port.in.commands.PostCommand;
import com.pjatk.posts.application.port.out.PostWritePort;
import com.pjatk.posts.core.domain.Post;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostCommandService implements PostCommand {
    private final PostWritePort postWritePort;
    @Override
    public String save(Post post) {
        return postWritePort.save(post);
    }

    @Override
    public Post update(String id, Post post) {
        return postWritePort.update(id, post);
    }

    @Override
    public boolean deleteById(String id) {
        return postWritePort.deleteById(id);
    }

    @Override
    public String changeLike(String id, Post.Like like) {
        return postWritePort.changeLike(id, like);
    }

    @Override
    public Integer addImage(String id, Post.Image image) {
        return postWritePort.addImage(id, image);
    }

    @Override
    public String deleteImage(String id, int source_id) {
        return postWritePort.deleteImage(id, source_id);
    }

    @Override
    public Integer addComment(String id, Post.Comment comment) {
        return postWritePort.addComment(id, comment);
    }

    @Override
    public String deleteComment(String id, int source_id) {
        return postWritePort.deleteComment(id, source_id);
    }

    @Override
    public Integer addImageToComment(String id, int source_id, Post.Image image) {
        return postWritePort.addImageToComment(id, source_id, image);
    }

    @Override
    public String deleteImageToComment(String id, int comment_id, int source_id) {
        return postWritePort.deleteImageToComment(id, comment_id, source_id);
    }

    @Override
    public String updateImage(String id, int imageId, Post.Image newImage) {
        return postWritePort.updateImage(id, imageId, newImage);
    }

    @Override
    public String updateComment(String id, int comment_id, Post.Comment newComment) {
        return postWritePort.updateComment(id, comment_id, newComment);
    }

    @Override
    public String updateImageInComment(String id, int commentId, int imageId, Post.Image newImage) {
        return postWritePort.updateImageInComment(id, commentId, imageId, newImage);
    }
}
