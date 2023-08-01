package com.umc.cons.post.dto;

import com.umc.cons.record.dto.RecordDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostWithRecordsDTO {
    private PostDTO postDTO;
    private List<RecordDTO> recordDTOList;
}