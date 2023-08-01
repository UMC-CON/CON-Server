package com.umc.cons.post.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.dto.*;
import com.umc.cons.post.service.PostService;
import com.umc.cons.record.dto.RecordDTO;
import com.umc.cons.record.dto.RecordResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.umc.cons.common.config.BaseResponseStatus.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/app/post")
public class PostController {
    private final PostService postService;

    // 게시글 등록 및 기록 생성(1개 이상)
    @PostMapping
    public BaseResponse<BaseResponseStatus> write(@RequestBody PostWithRecordsDTO postWithRecordsDTO){
        List<RecordDTO> recordDTOList = postWithRecordsDTO.getRecordDTOList();

        if (recordDTOList == null || recordDTOList.isEmpty()) {
            return new BaseResponse<>(REQUEST_ERROR);
        }

        Post savedPost = postService.saveWithRecords(postWithRecordsDTO.getPostDTO(), postWithRecordsDTO.getRecordDTOList());
        if (savedPost == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        return new BaseResponse<>(SUCCESS);
    }

    // 게시글 Id로 조회
    @GetMapping("/{id}")
    public BaseResponse<PostResponseDTO> readId(@PathVariable Long id){
        try {
            Post post = postService.findByIdAndNotDeleted(id);
            List<RecordResponseDTO> recordDTOLists = post.getRecords().stream()
                    .filter(record -> !record.isDeleted())
                    .map(RecordResponseDTO::of)
                    .collect(Collectors.toList());

            PostResponseDTO responseDTO = PostResponseDTO.convertToResponseDTO(post);
            responseDTO.setRecordList(recordDTOLists);
            return new BaseResponse<>(responseDTO);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 게시글 전체 조회 (페이징 처리)
    @GetMapping
    public BaseResponse<Page<PostResponseDTO>> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        Page<Post> posts = postService.findAllNotDeleted(pageable);
        Page<PostResponseDTO> responsePage = posts.map(post -> {
            List<RecordResponseDTO> recordDTOList = post.getRecords().stream()
                    .filter(record -> !record.isDeleted())
                    .map(RecordResponseDTO::of)
                    .collect(Collectors.toList());
            PostResponseDTO responseDTO = PostResponseDTO.convertToResponseDTO(post);
            responseDTO.setRecordList(recordDTOList);

            return responseDTO;
        });
        return new BaseResponse<>(responsePage);
    }

    // Member Id로 동일 멤버가 작성한 게시글을 리스트로 조회 (페이징 처리)
    @GetMapping("/member/{memberId}")
    public BaseResponse<Page<PostResponseDTO>> getPostsByMemberId(@PathVariable Long memberId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        try{
            Page<Post> posts = postService.getPostsByMemberIdAndNotDeleted(memberId, pageable);
            Page<PostResponseDTO> responsePage = posts.map(post -> {
                List<RecordResponseDTO> recordDTOList = post.getRecords().stream()
                        .filter(record -> !record.isDeleted())
                        .map(RecordResponseDTO::of)
                        .collect(Collectors.toList());
                PostResponseDTO responseDTO = PostResponseDTO.convertToResponseDTO(post);
                responseDTO.setRecordList(recordDTOList);

                return responseDTO;
            });

            return new BaseResponse<>(responsePage);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 게시글  수정
    @PatchMapping("/{id}")
    public BaseResponse<BaseResponseStatus> modify(@PathVariable Long id, @RequestBody PostDTO postDTO){
        Post modifiedPost = postService.modifyPost(id, postDTO);
        if (modifiedPost == null) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        return new BaseResponse<>(SUCCESS);
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public BaseResponse<BaseResponseStatus> delete(@PathVariable Long id){
        try{
            postService.delete(id);
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
    }
}