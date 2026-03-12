package com.pjatk.posts.application.port.in.queries;

import com.pjatk.posts.core.domain.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostQuery {
    List<Post> getPosts(Pageable pageable);
    List<Post> getPostsByName(String prefix, Pageable pageable);
    Optional<Post> getPostById(String id);
}
