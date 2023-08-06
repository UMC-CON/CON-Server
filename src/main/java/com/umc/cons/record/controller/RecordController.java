package com.umc.cons.record.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.service.PostService;
import com.umc.cons.record.domain.entity.Record;
import com.umc.cons.record.dto.RecordDTO;
import com.umc.cons.record.dto.RecordResponseDTO;
import com.umc.cons.record.service.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.cons.common.config.BaseResponseStatus.*;
import static com.umc.cons.common.config.BaseResponseStatus.RESPONSE_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/post/{postId}/record")
public class RecordController {
    private final RecordService recordService;
    private final PostService postService;

    // 기록 추가
    @PostMapping
    public BaseResponse<BaseResponseStatus> addRecords(@PathVariable Long postId, @RequestBody List<RecordDTO> recordDTOList){
        try{
            List<Record> records = recordService.createRecords(postId, recordDTOList);
            if (records == null || records.isEmpty()) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e){
            return new BaseResponse<>(REQUEST_ERROR);
        }
    }

    // 기록 수정
    @PatchMapping("/{id}")
    public BaseResponse<BaseResponseStatus> updateRecord(@PathVariable Long postId, @PathVariable Long id, @RequestBody RecordDTO recordDTO){
        try{
            Post post = postService.findByIdAndNotDeleted(postId);
            Record updatedRecord = recordService.updateRecord(id, recordDTO);
            if (updatedRecord == null) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e){
            return new BaseResponse<>(REQUEST_ERROR);
        }
    }

    // 기록 삭제
    @DeleteMapping("/{id}")
    public BaseResponse<BaseResponseStatus> deleteRecord(@PathVariable Long postId, @PathVariable Long id){
        try{
            Post post = postService.findByIdAndNotDeleted(postId);
            recordService.deleteRecord(id);
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e){
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 기록 조회
    @GetMapping
    public BaseResponse<List<RecordResponseDTO>> findAllRecord(@PathVariable Long postId){
        try{
            List<Record> records = recordService.findAllRecord(postId)
                    .stream()
                    .filter(record -> !record.isDeleted())
                    .collect(Collectors.toList());
            List<RecordResponseDTO> recordResponseDTOList = records.stream()
                    .map(RecordResponseDTO::of)
                    .collect(Collectors.toList());
            return new BaseResponse<>(recordResponseDTOList);
        } catch (IllegalArgumentException e){
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }
}