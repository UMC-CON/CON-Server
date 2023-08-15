package com.umc.cons.post.controller;

import com.umc.cons.common.annotation.LoginMember;
import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.service.ContentService;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.dto.*;
import com.umc.cons.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.umc.cons.common.config.BaseResponseStatus.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/app/post")
public class PostController {
    private final PostService postService;
    private final ContentService contentService;

    // 이미지 파일 업로드
    @PostMapping("/upload")
    public BaseResponse<String> uploadImage(@RequestParam("file") MultipartFile file) {
        if(file == null || file.isEmpty()) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
        try {
            String imageUrl = postService.uploadImage(file);
            return new BaseResponse<>(imageUrl);
        } catch (Exception e) {
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 게시글 등록
    @PostMapping
    public BaseResponse<BaseResponseStatus> write(@ModelAttribute PostDTO postDTO, @LoginMember Member member) {
        try {
            Content contents = contentService.findOne(postDTO.getContentId());
            if (contents == null) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            Post savedPost = postService.save(postDTO, contents, member);

            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
    }

    // 게시글 Id로 조회
    @GetMapping("/{id}")
    public BaseResponse<PostResponseDTO> readId(@PathVariable Long id){
        try{
            Post post = postService.findByIdAndNotDeleted(id);

            PostResponseDTO responseDTO = PostResponseDTO.convertToResponseDTO(post);
            return new BaseResponse<>(responseDTO);
        }catch (IllegalArgumentException e){
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 게시글 전체 조회 (페이징 처리)
    @GetMapping
    public BaseResponse<Page<PostResponseDTO>> findAll(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        Page<Post> posts = postService.findAllNotDeleted(pageable);
        Page<PostResponseDTO> responsePage = posts.map(PostResponseDTO::convertToResponseDTO);

        return new BaseResponse<>(responsePage);
    }

    // Member Id로 동일 멤버가 작성한 게시글을 리스트로 조회 (페이징 처리)
    @GetMapping("/member/{memberId}")
    public BaseResponse<Page<PostResponseDTO>> getPostsByMemberId(@PathVariable Long memberId, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        try{
            Page<Post> posts = postService.getPostsByMemberIdAndNotDeleted(memberId, pageable);
            Page<PostResponseDTO> responsePage = posts.map(PostResponseDTO::convertToResponseDTO);

            return new BaseResponse<>(responsePage);
        }catch (IllegalArgumentException e){
            return new BaseResponse<>(RESPONSE_ERROR);
        }
    }

    // 게시글  수정
    @PatchMapping("/{id}")
    public BaseResponse<BaseResponseStatus> modify(@PathVariable Long id, @ModelAttribute PostDTO postDTO){
        try{
            Post modifiedPost = postService.modifyPost(id, postDTO);

            if (modifiedPost == null) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e) {
            return new BaseResponse<>(REQUEST_ERROR);
        }
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