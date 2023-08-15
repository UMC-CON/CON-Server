package com.umc.cons.post.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.cons.common.util.BaseTimeEntity;
import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.post.dto.PostDTO;
import com.umc.cons.share.domain.entity.Share;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

@Entity(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    @JsonIgnore
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    @JsonIgnore
    private Content contents;

    @Column(nullable = false, name = "title", length = 10)
    private String title;

    @Column(nullable = false, name = "score")
    @Min(value = 0, message = "점수는 0보다 크거나 같아야 합니다.")
    @Max(value = 5, message = "점수는 5보다 작거나 같아야 합니다.")
    private double score;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "content")
    @Lob
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Share share;

    public void update(PostDTO postDTO, Content contents, Member member){
        this.member = member;
        this.contents = contents;
        this.title = postDTO.getTitle();
        this.score = postDTO.getScore();
        this.content = postDTO.getContent();
    }
}