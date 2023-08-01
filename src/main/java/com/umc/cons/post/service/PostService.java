package com.umc.cons.post.service;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.record.domain.entity.Record;
import com.umc.cons.post.domain.repository.PostRepository;
import com.umc.cons.record.domain.repository.RecordRepository;
import com.umc.cons.post.dto.PostDTO;
import com.umc.cons.record.dto.RecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final RecordRepository recordRepository;

    public Post findById(Long id){
        return postRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 글입니다."));
    }

    public Page<Post> getPostsByMemberId(Long memberId, Pageable pageable) {
        if(memberId == null || memberId == 0.0){
            throw new IllegalArgumentException("memberId가 없습니다.");
        }
        return postRepository.findByMemberId(memberId, pageable);
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    public Post saveWithRecords(PostDTO postDTO, List<RecordDTO> recordDTOList){
        Post post = new Post();
        post.update(postDTO);
        Post savedPost = postRepository.save(post);

        for(RecordDTO recordDTO : recordDTOList){
            Record record = new Record(recordDTO.getContent(), savedPost);
            recordRepository.save(record);
            savedPost.addRecord(record);
        }
        return savedPost;
    }

    @Transactional
    public Post modifyPost(Long id, PostDTO postDTO){
        Post existingPost = findById(id);
        // 변경 사항이 있을 경우에만 수정
        if (StringUtils.hasText(postDTO.getTitle())) {
            existingPost.setTitle(postDTO.getTitle());
        }
        if (postDTO.getImageUrl() != null) {
            existingPost.setImageUrl(postDTO.getImageUrl());
        }
        Double newScore = postDTO.getScore();
        if (newScore != null && newScore != 0) {
            existingPost.setScore(newScore);
        }

        return postRepository.save(existingPost);
    }

    @Transactional
    public void delete(Long id){
        Post post = findById(id);
        post.setDeleted(true);
        for(Record record : post.getRecords()){
            record.setDeleted(true);
        }
        postRepository.save(post);
    }

    // 게시물이 삭제되었는지 확인하는 메소드
    public Post findByIdAndNotDeleted(Long id) {
        Post post = findById(id);
        if(post.isDeleted()) {
            throw new IllegalArgumentException("삭제된 게시물입니다.");
        }
        return post;
    }

    public Page<Post> getPostsByMemberIdAndNotDeleted(Long memberId, Pageable pageable) {
        Page<Post> posts = getPostsByMemberId(memberId, pageable);
        List<Post> notDeletedPosts = new ArrayList<>();
        for (Post post : posts.getContent()) {
            if (!post.isDeleted()) {
                notDeletedPosts.add(post);
            }
        }
        return new PageImpl<>(notDeletedPosts, pageable, notDeletedPosts.size());
    }

    public Page<Post> findAllNotDeleted(Pageable pageable) {
        Page<Post> posts = findAll(pageable);
        // 검증을 여기서 추가
        List<Post> filteredPosts = new ArrayList<>();
        for (Post post : posts.getContent()) {
            if (!post.isDeleted()) {
                filteredPosts.add(post);
            }
        }
        return new PageImpl<>(filteredPosts, pageable, filteredPosts.size());
    }
}