package com.umc.cons.content.domain.entity;

import com.umc.cons.post.domain.entity.Post;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "content")
@Getter@Setter
public class Content {
    //id 값은 외부 DB값을 사용하므로 생성하지 않음
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name="name",nullable = false)
    private String name;
    @Column(name = "image",nullable = false)
    private String image;
    @Column(name = "genre",nullable = false)
    private String genre;
    @Column(name = "is_deleted",nullable = false)
    @ColumnDefault("false")
    private boolean is_deleted;
    @Column(name = "created_at",nullable = false)
    private LocalDateTime created_at;
    @Column(name = "modified_at",nullable = false)
    private LocalDateTime modified_at;

    @OneToMany(mappedBy = "content")
    private List<Post> posts = new ArrayList<>();
}
