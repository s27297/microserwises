package com.pjatk.posts.adapters.web;

import com.pjatk.posts.adapters.web.contract.posts.command.*;
import com.pjatk.posts.adapters.web.contract.posts.query.CommentDto;
import com.pjatk.posts.adapters.web.contract.posts.query.FullPostDto;
import com.pjatk.posts.adapters.web.contract.posts.query.ImageDto;
import com.pjatk.posts.adapters.web.contract.posts.query.PostDto;
import com.pjatk.posts.application.exceptions.NotFoundException;
import com.pjatk.posts.application.port.in.commands.PostCommand;
import com.pjatk.posts.application.port.in.queries.PostQuery;
import com.pjatk.posts.core.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostQuery postQuery;
    private final PostCommand postCommand;

    @GetMapping()
    public List<PostDto> getPosts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return postQuery.getPosts(PageRequest.of(page, size))
                .stream().map(PostDto::from).toList();
    }

    @GetMapping("/{id}")
    public PostDto getPost(
            @PathVariable("id") String id
    ) {
        Optional<Post> post = postQuery.getPostById(id);
        return post.map(PostDto::from).orElseThrow(()->
                new NotFoundException("post not found"));
    }

    @GetMapping("/full/{id}")
    public FullPostDto getFullPost(
            @PathVariable("id") String id
    ) {
        Optional<Post> post = postQuery.getPostById(id);
        return post.map(FullPostDto::from).orElseThrow(()->
                new NotFoundException("post not found"));
    }


    @GetMapping("/nazwa/full/{prefix}")
    public List<FullPostDto> getFullPostsByNazwaPrefix(
            @PathVariable("prefix") String prefix,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        System.out.println(prefix);
        List<Post> posts = postQuery.getPostsByName(prefix,PageRequest.of(page, size));
        return posts.stream().map(FullPostDto::from).toList();
    }

    @GetMapping("/nazwa/{prefix}")
    public List<PostDto> getPostsByNazwaPrefix(
            @PathVariable("prefix") String prefix,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        System.out.println(prefix);
        List<Post> posts = postQuery.getPostsByName(prefix,PageRequest.of(page, size));
        System.out.println(posts.size());
        return posts.stream().map(PostDto::from).toList();
    }

    @PostMapping()
    public ResponseEntity<String> savePost(@RequestBody CreatePostDto postDto
    ) {
        String id=postCommand.save(CreatePostDto.to(postDto));
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(
            @PathVariable("id") String id,
            @RequestBody CreatePostDto postDto
    ) {
        Post post=postCommand.update(id,CreatePostDto.to(postDto));
        if(post==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(PostDto.from(post), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable(name="id") String id
    ) {
        boolean d= postCommand.deleteById(id);
        if(d)
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        return new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
    }

    @PutMapping("/likes/{id}")
    public ResponseEntity<String> changeLikes(
            @PathVariable("id") String id,
            @RequestBody ChangeLikeDto likeDto
    ) {
        if(likeDto.value()<0||likeDto.value()>5) {
            return new ResponseEntity<>("like value must be between 0 and 5",HttpStatus.BAD_REQUEST);
        }
        String answer=postCommand.changeLike(id,ChangeLikeDto.to(likeDto));
        if(Objects.equals(answer, "post not found") || Objects.equals(answer, "like not found")) {
            return new ResponseEntity<>(answer,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(answer,HttpStatus.OK);
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<String> addImage(
            @PathVariable("id") String id,
            @RequestBody ImageDto imageDto
    ) {
        Integer answer=postCommand.addImage(id,ImageDto.to(imageDto));
        if(answer==null) {
            return new ResponseEntity<>("post not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Image added with source_id "+answer, HttpStatus.OK);
    }

    @DeleteMapping("/image")
    public ResponseEntity<String> deleteImage(
            @RequestBody DeleteDto deleteDto
    ) {
        String answer=postCommand.deleteImage(deleteDto.id(),deleteDto.source_id());
        if(Objects.equals(answer, "image was deleted")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comment/{id}")
    public ResponseEntity<String> addComment(
            @PathVariable("id") String id,
            @RequestBody CreateCommentDto createCommentDto
    ) {
        Integer answer=postCommand.addComment(id,CreateCommentDto.toComment(createCommentDto));
        if(answer==null) {
            return new ResponseEntity<>("post not found",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Comment added with source_id "+answer, HttpStatus.OK);
    }

    @DeleteMapping("/comment")
    public ResponseEntity<String> deleteComment(
            @RequestBody DeleteDto deleteDto
    ) {
        String answer=postCommand.deleteComment(deleteDto.id(),deleteDto.source_id());
        if(Objects.equals(answer, "comment was deleted")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comment/image/{id}")
    public ResponseEntity<String> addImageToComment(
            @PathVariable("id") String id,
            @RequestBody CreateImageToCommentDto createCommentDto
    ) {
        Integer answer=postCommand.addImageToComment(id,
                CreateImageToCommentDto.getCommentId(createCommentDto),
                CreateImageToCommentDto.to(createCommentDto));
        if(answer==null) {
            return new ResponseEntity<>("post not found",HttpStatus.NOT_FOUND);
        }
        if (answer==-1)
            return new ResponseEntity<>("comment not found",HttpStatus.NOT_FOUND);
        return new ResponseEntity<>("Image added with source_id "+answer, HttpStatus.OK);
    }

    @DeleteMapping("/comment/image")
    public ResponseEntity<String> deleteImageFromComment(
            @RequestBody DeleteImageFromCommentDto deleteDto
    ) {
        String answer=postCommand.deleteImageToComment(deleteDto.id(), deleteDto.comment_id(), deleteDto.source_id());
        if(Objects.equals(answer, "image was deleted")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }



    @PutMapping("/image/{id}")
    public ResponseEntity<String> updateImage(
            @PathVariable("id") String id,
            @RequestBody ImageDto imageDto
    ) {
        String answer=postCommand.updateImage(id,imageDto.source_id(),ImageDto.to(imageDto));
        if(Objects.equals(answer, "image was updated")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<String> updateComment(
            @PathVariable("id") String id,
            @RequestBody UpdateCommentDto updateCommentDto
    ) {
        String answer=postCommand.updateComment(id,updateCommentDto.source_id(),UpdateCommentDto.to(updateCommentDto));
        if(Objects.equals(answer, "comment was updated")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
        return new ResponseEntity<>(answer, HttpStatus.NOT_FOUND);
    }

    @PutMapping("/comment/image/{id}")
    public ResponseEntity<String> updateImageInComment(
            @PathVariable("id") String id,
            @RequestBody UpdateImageInCommentDto updateImageInCommentDto
    ) {
        String answer=postCommand.updateImageInComment(id,
                UpdateImageInCommentDto.getCommentId(updateImageInCommentDto),
                updateImageInCommentDto.source_id(),
                UpdateImageInCommentDto.to(updateImageInCommentDto));
        if(Objects.equals(answer, "image was updated")) {
            return new ResponseEntity<>(answer,HttpStatus.OK);
        }
            return new ResponseEntity<>(answer,HttpStatus.NOT_FOUND);
    }

}
