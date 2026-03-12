package com.pjatk.posts.adapters.persistance.repositories;

import com.pjatk.posts.adapters.persistance.model.PostDocument;
import com.pjatk.posts.core.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PostRepository extends MongoRepository<PostDocument, String> {
    @Query(value = "{ 'nazwa': { $regex: ?0, $options: 'i' } }")
    List<PostDocument> findByNazwaPrefixRegex(String anchoredPrefixRegex, Pageable pageable);
}
