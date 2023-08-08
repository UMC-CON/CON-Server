package com.umc.cons.post.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.domain.repository.PostRepository;
import com.umc.cons.post.dto.PostDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    @Value("${application.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    public String uploadImage(MultipartFile file){
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));
        fileObj.delete();
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileName;
    }

    private File convertMultiPartFileToFile(MultipartFile file){
        File convertedFile = new File(file.getOriginalFilename());
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error convertiong multipartFile to file", e);
        }
        return convertedFile;
    }

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
    public Post save(PostDTO postDTO){
        // memberId, contentId, title, content 값이 없으면 예외 처리
        if (postDTO.getMemberId() == null || postDTO.getContentId() == null ||
                !StringUtils.hasText(postDTO.getTitle()) || !StringUtils.hasText(postDTO.getContent())) {
            throw new IllegalArgumentException("필수 입력 사항이 누락되었습니다.");
        }

        Post post = new Post();
        post.update(postDTO);

        Post savedPost = postRepository.save(post);

        if (postDTO.getImageUrl() != null) {
            // 만약 DTO에서 imageUrl이 제공되면, 그 값을 직접 사용
            savedPost.setImageUrl(postDTO.getImageUrl());
        } else if (postDTO.getImageFile() != null && !postDTO.getImageFile().isEmpty()) {
            // 만약 imageFile이 제공되면, 이미지를 업로드하고 저장
            try {
                String imageUrl = uploadImage(postDTO.getImageFile());
                savedPost.setImageUrl(imageUrl);
            } catch (Exception e) {
                log.error("이미지 업로드 오류", e);
                return null;
            }
        }

        return savedPost;
    }

    @Transactional
    public Post modifyPost(Long id, PostDTO postDTO){
        Post existingPost = findByIdAndNotDeleted(id);

        // 변경 사항이 있을 경우에만 수정
        if (StringUtils.hasText(postDTO.getTitle())) {
            existingPost.setTitle(postDTO.getTitle());
        }
        Double newScore = postDTO.getScore();
        if (newScore != null && newScore != 0) {
            existingPost.setScore(newScore);
        }
        if (StringUtils.hasText(postDTO.getContent())) {
            existingPost.setContent(postDTO.getContent());
        }
        else if (postDTO.getContent() != null && postDTO.getContent().isEmpty()) {
            existingPost.setContent(postDTO.getContent());
        }

        String newImageUrl = postDTO.getImageUrl();
        MultipartFile newImageFile = postDTO.getImageFile();

        if (StringUtils.hasText(newImageUrl)) {
            existingPost.setImageUrl(newImageUrl);
        }
        else if (newImageFile != null && !newImageFile.isEmpty()) {
            try {
                String imageUrl = uploadImage(newImageFile);
                existingPost.setImageUrl(imageUrl);
            } catch (Exception e) {
                log.error("이미지 업로드 오류", e);
                return null;
            }
        } else if (newImageUrl != null && newImageUrl.isEmpty()) {
            existingPost.setImageUrl(null);
        }

        return postRepository.save(existingPost);
    }

    @Transactional
    public void delete(Long id){
        Post post = findByIdAndNotDeleted(id);
        post.setDeleted(true);
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