package com.umc.cons.content.domain.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name = "content")
@Getter@Setter
public class Content {
    //TDD: generated value 에 대한 고민 필요
    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name="name")
    private String name;
    @Column(name = "image")
    private String image;
    @Column(name = "genre")
    private String genre;
    @Column(name = "created_at")
    private LocalDateTime created_at;
    @Column(name = "modified_at")
    private LocalDateTime modified_at;


}
