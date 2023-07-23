package com.umc.cons.member.domain.entity;

import com.umc.cons.common.util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "email")
    private String email;

    private String password;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted;
}
