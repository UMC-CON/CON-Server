package com.umc.cons.record.dto;

import com.umc.cons.record.domain.entity.Record;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecordResponseDTO {
    private String content;

    public static RecordResponseDTO of(Record record){
        return RecordResponseDTO.builder()
                .content(record.getContent())
                .build();
    }
}