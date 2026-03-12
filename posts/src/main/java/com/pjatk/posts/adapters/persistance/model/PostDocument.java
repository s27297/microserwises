package com.pjatk.posts.adapters.persistance.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("posts")
public class PostDocument {
    @Id
    private String id;
    private String nazwa;
    private String opis;
    @Field("date_of_creation") private Date dateOFCreating;

    private List<Comment> comments;
    private List<Image> images;
    private List<Like> likes;

    public record Like(Integer source_id,String user_id,int value) {}
    public record Image(Integer source_id,String nazwa,String url) {}
    public record Comment(Integer source_id,String user_id,String text,List<Image> images,String reply) {}

}


