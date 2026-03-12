package com.pjatk.posts.core.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
        @Id
        private String id;
        private String nazwa;
        private String opis;
        private Date dateOFCreating;

        private List<Comment> comments;
        private List<Image> images;
        private List<Like> likes;

        public record Like(Integer source_id,String user_id,int value) {}
        public record Image(Integer source_id,String nazwa,String url) {}
        public record Comment(Integer source_id,String user_id,String text,List<Image> images,String reply) {}

        public Post(String id,String nazwa,String opis,Date dateOFCreating){
         this.id = id;
         this.nazwa = nazwa;
         this.opis = opis;
         this.dateOFCreating = dateOFCreating;
        }



}
