package com.umc.cons.member.domain.entity;

import com.umc.cons.common.util.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
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

    @Column(nullable = false, name = "is_deleted")
    @Builder.Default
    private boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(Long id, String email, String password, String imageUrl, String name) {
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.name = name;
    }
}
