package com.umc.cons.share.service;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.service.PostService;
import com.umc.cons.post.domain.repository.PostRepository;
import com.umc.cons.share.domain.entity.Share;
import com.umc.cons.share.domain.repository.ShareRepository;
import com.umc.cons.share.dto.ShareDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ShareService {
    private final ShareRepository shareRepository;
    private final PostRepository postRepository;
    private final PostService postService;

    @Transactional
    public Share sharePost(Long postId, String comment){
        Post post = postService.findById(postId);

        if(post.getShare() != null) {
            throw new DataIntegrityViolationException("이미 공유한 콘텐츠입니다.");
        }

        Share share = new Share(comment, post);
        post.setShare(share);
        return shareRepository.save(share);
    }

    @Transactional
    public Share updateShare(Long postId, ShareDTO shareDTO){
        Post post = postService.findById(postId);
        Share share = post.getShare();
        share.update(shareDTO);
        return  share;
    }

    @Transactional
    public void deleteShare(Long postId){
        Post post = postService.findById(postId);
        Share share = post.getShare();

        if(share == null) {
            throw new IllegalArgumentException("공유된 콘텐츠가 아닙니다.");
        }

        // 게시글 공유 삭제 후 post와 share 연결 해제
        share.setDeleted(true);
        shareRepository.save(share);
        post.setShare(null);
        postRepository.save(post);
    }

    public Page<Share> findAllSharedPosts(Pageable pageable){
        return shareRepository.findAllBy(pageable);
    }
}