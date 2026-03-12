package com.pjatk.posts.application;

import com.pjatk.posts.application.port.in.queries.PostQuery;
import com.pjatk.posts.application.port.out.PostReadPort;
import com.pjatk.posts.core.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class PostsService implements PostQuery {
    private final PostReadPort postReadPort;
    @Override
    public List<Post> getPosts(Pageable pageable) {
        return postReadPort.getPosts(pageable);
    }

    @Override
    public List<Post> getPostsByName(String prefix, Pageable pageable) {
        return postReadPort.getPostByNames(prefix, pageable);
    }


    @Override
    public Optional<Post> getPostById(String id) {
        return postReadPort.getPostById(id);
    }
}
