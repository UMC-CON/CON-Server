package com.umc.cons.record.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.cons.common.util.BaseTimeEntity;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.record.dto.RecordDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity(name = "record")
@Getter
@Setter
@NoArgsConstructor
public class Record extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "content", columnDefinition = "CLOB")
    private String content;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

    public Record(String content, Post post){
        this.content = content;
        this.post = post;
    }
}