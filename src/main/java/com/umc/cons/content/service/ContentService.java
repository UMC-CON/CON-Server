package com.umc.cons.content.service;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.domain.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
}
