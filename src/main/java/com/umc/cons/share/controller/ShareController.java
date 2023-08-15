package com.umc.cons.share.controller;

import com.umc.cons.common.config.BaseResponse;
import com.umc.cons.common.config.BaseResponseStatus;
import com.umc.cons.share.domain.entity.Share;
import com.umc.cons.share.dto.ShareDTO;
import com.umc.cons.share.dto.ShareResponseDTO;
import com.umc.cons.share.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.umc.cons.common.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/post/share")
public class ShareController {
    private final ShareService shareService;

    // 공유글 등록
    @PostMapping("/{postId}")
    public BaseResponse<BaseResponseStatus> sharePost(@PathVariable Long postId, @RequestBody ShareDTO shareDTO){
        try{
            Share share = shareService.sharePost(postId, shareDTO.getComment());
            if (share == null) {
                return new BaseResponse<>(REQUEST_ERROR);
            }
            return new BaseResponse<>(SUCCESS);
        } catch (DataIntegrityViolationException e){
            return new BaseResponse<>(false, e.getMessage(), HttpStatus.CONFLICT.value(), null);
        }
    }

    //공유글 수정
    @PatchMapping("/{postId}")
    public BaseResponse<BaseResponseStatus> update(@PathVariable Long postId, @RequestBody ShareDTO shareDTO){
        Share updatedShare = shareService.updateShare(postId, shareDTO);
        if (updatedShare == null) {
            return new BaseResponse<>(RESPONSE_ERROR);
        }
        return new BaseResponse<>(SUCCESS);
    }

    // 공유글 삭제
    @DeleteMapping("/{postId}")
    public BaseResponse<BaseResponseStatus> deleteShare(@PathVariable Long postId){
        try{
            shareService.deleteShare(postId);
            return new BaseResponse<>(SUCCESS);
        } catch (IllegalArgumentException e){
            return new BaseResponse<>(REQUEST_ERROR);
        }
    }

    // 공유 된 글 조회 (페이징 처리)
    @GetMapping
    public BaseResponse<Page<ShareResponseDTO>> findAllSharedPosts(Pageable pageable){
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());
        Page<Share> sharedPosts =shareService.findAllSharedPosts(pageable);
        Page<ShareResponseDTO> shareResponseDTOs = sharedPosts.map(ShareResponseDTO::of);
        return new BaseResponse<>(shareResponseDTOs);
    }
}