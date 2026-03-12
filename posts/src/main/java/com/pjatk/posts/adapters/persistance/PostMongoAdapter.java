package com.pjatk.posts.adapters.persistance;

import com.pjatk.posts.adapters.persistance.mappers.PostMapper;
import com.pjatk.posts.adapters.persistance.model.PostDocument;
import com.pjatk.posts.adapters.persistance.repositories.PostRepository;
import com.pjatk.posts.application.port.out.PostReadPort;
import com.pjatk.posts.application.port.out.PostWritePort;
import com.pjatk.posts.core.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class PostMongoAdapter implements PostWritePort, PostReadPort {
    private final PostRepository repo;

    @Override
    public List<Post> getPosts(Pageable pageable) {
        return repo.findAll(pageable).stream().map(PostMapper::toPostDomain).toList();
    }

    @Override
    public List<Post> getPostByNames(String prefix, Pageable pageable) {
        String pattern = (prefix == null || prefix.isBlank())
                ? ".*"
                : "^" + Pattern.quote(prefix);

        return repo.findByNazwaPrefixRegex(pattern, pageable).stream()
                .map(PostMapper::toPostDomain)
                .toList();
    }


    @Override
    public Optional<Post> getPostById(String id) {
        return repo.findById(id).map(PostMapper::toPostDomain);
    }

    @Override
    public String save(Post post) {

        return repo.save(PostMapper.toPostDocument(post)).getId();
    }

    @Override
    public Post update(String id, Post post) {
        PostDocument postDocument=repo.findById(id).orElse(null);
        if (postDocument == null) {
            return null;
        }
        postDocument.setNazwa(post.getNazwa());
        postDocument.setOpis(post.getOpis());
        repo.save(postDocument);
        return PostMapper.toPostDomain(postDocument);
    }

    @Override
    public boolean deleteById(String id) {
        if(repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public String changeLike(String id, Post.Like like) {
        System.out.println(like.value());
        PostDocument postDocument=repo.findById(id).orElse(null);
        if (postDocument == null)  return "post not found";

        List<PostDocument.Like> l= postDocument.getLikes().stream()
                .filter(a -> a.user_id() != null && a.user_id().equals(like.user_id()))
                .toList();
            if(like.value()>0) {
                if (!l.isEmpty()) {
                    List<PostDocument.Like> updated = postDocument.getLikes().stream()
                            .map(a -> (a.user_id() != null && a.user_id().equals(like.user_id()))
                                    ? new PostDocument.Like(a.source_id(), a.user_id(), like.value())
                                    : a)
                            .collect(Collectors.toCollection(ArrayList::new));
                    postDocument.setLikes(updated);
                    repo.save(postDocument);
                    return "like was changed";
                }
                int nextId = getNextLikeIndex(postDocument.getLikes());
                PostDocument.Like likeDocument = new PostDocument.Like(nextId, like.user_id(), like.value());
                postDocument.getLikes().add(likeDocument);
                repo.save(postDocument);
                return "like was added with source id " + nextId;
            }
            else {
                if (!l.isEmpty()) {
                    PostDocument.Like toRemove = l.get(0);
                    postDocument.getLikes().removeIf(a -> Objects.equals(a.user_id(), like.user_id()));
                    repo.save(postDocument);
                    return "like was deleted";
                } else {
                    return "like not found";
                }
            }


    }

    @Override
    public Integer addImage(String id, Post.Image image) {
            PostDocument postDocument = repo.findById(id).orElse(null);
            if (postDocument == null) return null;

            if(postDocument.getImages() == null)
                postDocument.setImages(new ArrayList<>());
            int sourceId = getNextImageIndex(postDocument.getImages());
            PostDocument.Image imgDoc = new PostDocument.Image(sourceId, image.nazwa(), image.url());
        postDocument.getImages().add(imgDoc);
            repo.save(postDocument);
            return sourceId;
    }

    @Override
    public String deleteImage(String id, int source_id) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";


        boolean removed = postDocument.getImages().removeIf(a -> a.source_id() == source_id);
        if (removed) {
            repo.save(postDocument);
            return "image was deleted";
        } else {
            return "image not found";
        }
    }

    @Override
    public Integer addComment(String id, Post.Comment comment) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return null;

        if(postDocument.getComments() == null)
            postDocument.setComments(new ArrayList<>());
        int sourceId = getNextCommentIndex(postDocument.getComments());
        PostDocument.Comment commentDoc = new PostDocument.Comment(
                sourceId,
                comment.user_id(),
                comment.text(),
                new ArrayList<>(),
                comment.reply()
        );
        postDocument.getComments().add(commentDoc);
        repo.save(postDocument);
        return sourceId;
    }

    @Override
    public String deleteComment(String id, int source_id) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";

        if(postDocument.getComments() == null)
            postDocument.setComments(new ArrayList<>());
        boolean removed = postDocument.getComments().removeIf(a -> a.source_id() == source_id);
        if (removed) {
            repo.save(postDocument);
            return "comment was deleted";
        } else {
            return "comment not found";
        }
    }



    @Override
    public Integer addImageToComment(String id, int source_id, Post.Image image) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return null;

        if(postDocument.getComments() == null)
            postDocument.setComments(new ArrayList<>());

        PostDocument.Comment target = postDocument.getComments().stream()
                .filter(c -> c.source_id() == source_id)
                .findFirst()
                .orElse(null);
        if (target == null) return -1;

        List<PostDocument.Image> commentImages = Optional.ofNullable(target.images()).orElseGet(ArrayList::new);

        int imageId = getNextImageIndex(commentImages);
        PostDocument.Image newImg = new PostDocument.Image(imageId, image.nazwa(), image.url());
        commentImages.add(newImg);

        PostDocument.Comment updatedComment = new PostDocument.Comment(
                target.source_id(),
                target.text(),
                target.user_id(),
                new ArrayList<>(commentImages),
                target.reply()
        );

        List<PostDocument.Comment> updatedComments = postDocument.getComments().stream()
                .map(c -> c.source_id() == source_id ? updatedComment : c)
                .collect(Collectors.toCollection(ArrayList::new));
        postDocument.setComments(updatedComments);
        repo.save(postDocument);
        return imageId;
    }

    @Override
    public String deleteImageToComment(String id, int comment_id, int source_id) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";

        if(postDocument.getComments() == null)
            postDocument.setComments(new ArrayList<>());

        PostDocument.Comment target = postDocument.getComments().stream()
                .filter(c -> c.source_id() == comment_id)
                .findFirst()
                .orElse(null);
        if (target == null) return "comment not found";

        List<PostDocument.Image> commentImages = Optional.ofNullable(target.images()).orElseGet(ArrayList::new);
        boolean removed = commentImages.removeIf(img -> img.source_id() == source_id);
        if (!removed) return "image not found";

        PostDocument.Comment updatedComment = new PostDocument.Comment(
                target.source_id(),
                target.text(),
                target.user_id(),
                new ArrayList<>(commentImages),
                target.reply()
        );

        List<PostDocument.Comment> updatedComments = postDocument.getComments().stream()
                .map(c -> c.source_id() == comment_id ? updatedComment : c)
                .collect(Collectors.toCollection(ArrayList::new));
        postDocument.setComments(updatedComments);
        repo.save(postDocument);
        return "image was deleted";
    }

    @Override
    public String updateImage(String id, int imageId, Post.Image newImage) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";
        if(postDocument.getImages() == null)
            postDocument.setImages(new ArrayList<>());


        List<PostDocument.Image> images = postDocument.getImages();

        boolean exists = images.stream().anyMatch(i -> i.source_id() == imageId);
        if (!exists) return "image not found";

        postDocument.setImages(
                images.stream()
                        .map(i -> i.source_id() == imageId
                                ? new PostDocument.Image(imageId, newImage.nazwa(), newImage.url())
                                : i)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        System.out.println(imageId+"  "+newImage.nazwa()+"  "+newImage.url());
        repo.save(postDocument);
        return "image was updated";
    }

    @Override
    public String updateComment(String id, int comment_id, Post.Comment newComment) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";
        if(postDocument.getComments() == null)
            postDocument.setImages(new ArrayList<>());


        List<PostDocument.Comment> comments = postDocument.getComments();

        PostDocument.Comment exists = comments.stream().filter(i -> i.source_id() == comment_id).toList().stream().findFirst().orElse(null);
        if (exists==null) return "comment not found";
        postDocument.setComments(
                comments.stream()
                        .map(i -> i.source_id() == comment_id
                                ? new PostDocument.Comment(comment_id, newComment.user_id(),newComment.text(),exists.images(),newComment.reply())
                                : i)
                        .collect(Collectors.toCollection(ArrayList::new))
        );
        repo.save(postDocument);
        return "comment was updated";
    }

    @Override
    public String updateImageInComment(String id,int commentId,
                                       int imageId, Post.Image newImage) {
        PostDocument postDocument = repo.findById(id).orElse(null);
        if (postDocument == null) return "post not found";

        PostDocument.Comment target = postDocument.getComments().stream()
                .filter(c -> c.source_id() == commentId)
                .findFirst()
                .orElse(null);

        if (target == null) return "comment not found";

        List<PostDocument.Image> imgs = target.images() != null
                ? new ArrayList<>(target.images())
                : new ArrayList<>();

        boolean exists = imgs.stream().anyMatch(i -> i.source_id() == imageId);
        if (!exists) return "image not found";

        List<PostDocument.Image> updatedImages =
                imgs.stream()
                        .map(i -> i.source_id() == imageId
                                ? new PostDocument.Image(imageId, newImage.nazwa(), newImage.url())
                                : i)
                        .collect(Collectors.toCollection(ArrayList::new));

        PostDocument.Comment updatedComment = new PostDocument.Comment(
                target.source_id(),
                target.text(),
                target.user_id(),
                updatedImages,
                target.reply()
        );

        List<PostDocument.Comment> updatedComments =
                postDocument.getComments().stream()
                        .map(c -> c.source_id() == commentId ? updatedComment : c)
                        .collect(Collectors.toCollection(ArrayList::new));

        postDocument.setComments(updatedComments);
        repo.save(postDocument);
        return "image was updated";
    }






        private static Integer getNextImageIndex(List<PostDocument.Image> list){
        return list.stream().mapToInt(PostDocument.Image::source_id).max().orElse(0) + 1;


    }

    private static Integer getNextCommentIndex(List<PostDocument.Comment> list){
        return list.stream().mapToInt(PostDocument.Comment::source_id).max().orElse(0) + 1;

    }

    private static Integer getNextLikeIndex(List<PostDocument.Like> list){
        return list.stream().mapToInt(PostDocument.Like::source_id).max().orElse(0) + 1;

    }


}
