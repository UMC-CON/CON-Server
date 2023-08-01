package com.umc.cons.record.service;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.service.PostService;
import com.umc.cons.record.domain.entity.Record;
import com.umc.cons.record.domain.repository.RecordRepository;
import com.umc.cons.record.dto.RecordDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordService {
    private final RecordRepository recordRepository;
    private final PostService postService;

    // 기록 생성
    @Transactional
    public List<Record> createRecords(Long postId, List<RecordDTO> recordDTOList){
        Post post = postService.findById(postId);
        List<Record> records = new ArrayList<>();

        for(RecordDTO recordDTO : recordDTOList){
            Record record = new Record(recordDTO.getContent(), post);
            records.add(recordRepository.save(record));
        }

        return records;
    }

    // 기록 수정
    @Transactional
    public Record updateRecord(Long id, RecordDTO recordDTO){
        Record existingRecord = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 기록을 찾을 수 없습니다."));
        if(StringUtils.hasText(recordDTO.getContent())){
            existingRecord.setContent(recordDTO.getContent());
        }
        return recordRepository.save(existingRecord);
    }

    // 기록 삭제
    public void deleteRecord(Long id){
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 기록을 찾을 수 없습니다."));
        if(record.isDeleted()){
            throw  new IllegalArgumentException();
        }
        record.setDeleted(true);
        recordRepository.save(record);
    }

    // 게시글에 해당하는 모든 기록 조회
    public List<Record> findAllRecord(Long postId){
        Post post = postService.findById(postId);
        return recordRepository.findAllByPost(post);
    }
}