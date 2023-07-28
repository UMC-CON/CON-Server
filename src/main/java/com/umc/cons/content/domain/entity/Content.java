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
//    @GeneratedValue
    @Column(name = "id")
    private Long id;

    private String name;

    private String image;

    private String genre;

    private LocalDateTime created_at;

    private LocalDateTime modified_at;


}
