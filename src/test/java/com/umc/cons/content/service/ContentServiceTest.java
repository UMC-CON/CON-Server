package com.umc.cons.content.service;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.domain.repository.ContentRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.lang.reflect.Member;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ContentServiceTest {
    @Autowired ContentService contentService;
    @Autowired ContentRepository contentRepository;

    @Test
    public void 콘텐츠추가() throws Exception{
        //given
        Content content = new Content();
        content.setId(1L);
        content.setName("Example Content 1");
        content.setImage("example1.jpg");
        content.setGenre("Action");
        content.setModified_at(LocalDateTime.now());
        content.setCreated_at(LocalDateTime.now());
        //when
        Long savedId = contentService.join(content);

        //then
        assertEquals(content, contentRepository.findOne(savedId));

    }
    @Test
    public void 중복회원확인() throws Exception{
        //given
        Long duplicatedId = 1L;
        Content content1 = new Content();
        content1.setId(duplicatedId);
        content1.setName("Example Content 1");
        content1.setImage("example1.jpg");
        content1.setGenre("Action");
        content1.setModified_at(LocalDateTime.now());
        content1.setCreated_at(LocalDateTime.now());
        Content content2 = new Content();
        content2.setId(duplicatedId);
        content2.setName("Example Content 2");
        content2.setImage("example2.jpg");
        content2.setGenre("Social");
        //when
        contentService.join(content1);
        contentService.join(content2);

        //then
        // content1 만이 저장되어 있어야 함
        assertEquals(content1,contentRepository.findOne(duplicatedId));
    }

}