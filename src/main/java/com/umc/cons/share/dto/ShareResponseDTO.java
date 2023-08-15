package com.umc.cons.share.dto;

import com.umc.cons.share.domain.entity.Share;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ShareResponseDTO {
    private String comment;

    public static ShareResponseDTO of(Share share){
        return ShareResponseDTO.builder()
                .comment(share.getComment())
                .build();
    }
}