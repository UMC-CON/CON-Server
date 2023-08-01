package com.umc.cons.share.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.cons.common.util.BaseTimeEntity;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.share.dto.ShareDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "share")
@Getter
@Setter
@NoArgsConstructor
public class Share extends BaseTimeEntity {
    @Id
    @Column(name = "post_id")
    private Long postId;

    @Column( nullable = false, name = "comment", length = 30)
    private String comment; // 한줄평

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    public Share(String comment, Post post){
        this.comment = comment;
        this.post = post;
    }

    public void update(ShareDTO shareDTO){
        this.comment = shareDTO.getComment();
    }
}