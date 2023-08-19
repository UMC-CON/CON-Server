package com.umc.cons.follow.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.awt.*;
import java.io.Serializable;

@Entity @Table(name = "follow")
@Data
@Embeddable
public class Follow implements Serializable {
    @EmbeddedId
    private FollowId followId;
}
