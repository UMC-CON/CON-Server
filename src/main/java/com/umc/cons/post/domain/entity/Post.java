package com.umc.cons.post.domain.entity;

import com.umc.cons.common.util.BaseTimeEntity;
import com.umc.cons.post.dto.PostDTO;
import com.umc.cons.record.domain.entity.Record;
import com.umc.cons.share.domain.entity.Share;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "member_id")
    private Long memberId;

    @Column(nullable = false, name = "content_id")
    private Long contentId;

    @Column(nullable = false, name = "title", length = 10)
    private String title;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false, name = "score")
    @Min(value = 0, message = "점수는 0보다 크거나 같아야 합니다.")
    @Max(value = 5, message = "점수는 5보다 작거나 같아야 합니다.")
    private double score;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Share share;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Record> records = new ArrayList<>();

    public void addRecord(Record record) {
        records.add(record);
        record.setPost(this);
    }

    public void update(PostDTO postDTO){
        this.memberId = postDTO.getMemberId();
        this.contentId = postDTO.getContentId();
        this.title = postDTO.getTitle();
        this.imageUrl = postDTO.getImageUrl();
        this.score = postDTO.getScore();
    }
}