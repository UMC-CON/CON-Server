package com.umc.cons.post.dto;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.record.domain.entity.Record;
import com.umc.cons.record.dto.RecordResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostResponseDTO {
    private String title;
    private LocalDate createdAt;
    private double score;
    private String imageUrl;
    private List<RecordResponseDTO> recordList;

    public static PostResponseDTO convertToResponseDTO(Post post){
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setTitle(post.getTitle());
        responseDTO.setImageUrl(post.getImageUrl());
        responseDTO.setScore(post.getScore());
        responseDTO.setCreatedAt(post.getCreatedAt().toLocalDate());

        List<Record> records = post.getRecords();
        List<RecordResponseDTO> recordDTOList = new ArrayList<>();

        for(Record record : records){
            RecordResponseDTO recordDTO = RecordResponseDTO.of(record);
            recordDTOList.add(recordDTO);
        }

        responseDTO.setRecordList(recordDTOList);

        return responseDTO;
    }
}