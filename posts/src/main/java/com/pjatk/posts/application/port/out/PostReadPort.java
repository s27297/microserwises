package com.pjatk.posts.application.port.out;

import com.pjatk.posts.core.domain.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostReadPort {
    List<Post> getPosts(Pageable pageable);
    List<Post> getPostByNames(String name,Pageable pageable);
    Optional<Post> getPostById(String id);

}
