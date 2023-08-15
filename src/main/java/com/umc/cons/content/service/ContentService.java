package com.umc.cons.content.service;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.domain.repository.ContentRepository;
import com.umc.cons.content.dto.ContentRequestDto;
import com.umc.cons.content.dto.ContentResponseDto;
import com.umc.cons.content.dto.MultipleContentRequestDto;
import com.umc.cons.content.dto.MultipleContentResponseDto;
import com.umc.cons.content.exception.NotExistContentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ContentService {

    private final ContentRepository contentRepository;

    //content 저장 로직
    @Transactional
    public Long join(Content content){
        //중복 회원 검증
        if(validateDuplicateMember(content)){
            contentRepository.save(content);
        }
        return content.getId();
    }

    private boolean validateDuplicateMember(Content content) {
        Content findContent = contentRepository.findOne(content.getId());
        return findContent == null;
    }

    //특정 content 조회
    public Content findOne(Long memberId) {
        return contentRepository.findOne(memberId);
    }

    //생성 메서드
    /**
     * 콘텐츠 생성
     */
    @Transactional
    public  ContentResponseDto createContent(ContentRequestDto requestDto){
        //생성
        Content content = new Content();
        content.setId(requestDto.getId());content.setName(requestDto.getName());content.setImage(requestDto.getImage());
        content.setGenre(requestDto.getGenre());
        content.setCreated_at(LocalDateTime.now());content.setModified_at(LocalDateTime.now());
        //저장
        Long savedID = this.join(content);
        Content savedContent = findOne(savedID);
        //response
        ContentResponseDto contentResponseDto = new ContentResponseDto();
        contentResponseDto.setId(savedContent.getId());
        contentResponseDto.setName(savedContent.getName());
        contentResponseDto.setImage(savedContent.getImage());
        contentResponseDto.setGenre(savedContent.getGenre());
        return contentResponseDto;
    }
    //생성 메서드
    /**
     * 콘텐츠 복수개 생성
     */
    @Transactional
    public MultipleContentResponseDto createContents(MultipleContentRequestDto multipleContentRequestDto){
        List<ContentResponseDto> contentResponseDtos = new ArrayList<>();
        for(ContentRequestDto contentRequestDto:multipleContentRequestDto.getContents()){
            contentResponseDtos.add(createContent(contentRequestDto));
        }
        return new MultipleContentResponseDto(contentResponseDtos);
    }
    //조회 메서드
    /**
     * 콘텐츠 정보 조회
     */
    public ContentResponseDto getContent(Long id){
        Content content = contentRepository.findOne(id);
        if(content ==null){
            throw new NotExistContentException("조회할 콘텐츠가 데이터베이스에 존재하지 않습니다.");
        }
        ContentResponseDto contentResponseDto = new ContentResponseDto();
        contentResponseDto.setId(content.getId());
        contentResponseDto.setName(content.getName());
        contentResponseDto.setImage(content.getImage());
        contentResponseDto.setGenre(content.getGenre());
        return contentResponseDto;
    }
    //수정 메서드
    /**
     * 콘텐츠 정보 업데이트
     */
    @Transactional
    public Content updateContent(Long id, String name, String image, String genre){
        Content content = contentRepository.findOne(id);
        if (content == null){
            throw new NotExistContentException("수정할 콘텐츠가 데이터베이스에 존재하지 않습니다.");
        }
        content.setName(name);
        content.setImage(image);
        content.setGenre(genre);
        content.setModified_at(LocalDateTime.now());
        return content;
    }

}
