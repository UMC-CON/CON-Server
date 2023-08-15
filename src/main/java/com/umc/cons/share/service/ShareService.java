package com.umc.cons.share.service;

import com.umc.cons.ConsApplication;
import com.umc.cons.content.domain.repository.ContentRepository;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.service.PostService;
import com.umc.cons.post.domain.repository.PostRepository;
import com.umc.cons.share.domain.entity.Share;
import com.umc.cons.share.domain.repository.ShareRepository;
import com.umc.cons.share.dto.ShareDTO;
import com.umc.cons.share.util.ScoredShare;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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


    public double cosineSimilarity(BitSet A, BitSet B){

        BitSet C = (BitSet) A.clone();
        C.and(B);
        double mag_A = Math.sqrt(A.cardinality());
        double mag_B = Math.sqrt(B.cardinality());
        return C.cardinality()/(mag_A*mag_B);

    }

    public List<Share> recommendedShares(long userId){

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ConsApplication.class);
        ContentRepository contentRepository = ac.getBean(ContentRepository.class);
        MemberRepository memberRepository = ac.getBean(MemberRepository.class);

        //사용자 스코어
        //결과물 저장할 리스트
        List<Share> recommendedShares = new LinkedList<>();
        //사용자 별 점수 저장할 해시맵
        Map<Integer, Double> scoreByUserMap = new HashMap<>();
        Map<Integer, Double> scoreByContentMap = new LinkedHashMap<>();

        int maxContentId = Math.toIntExact(contentRepository.findLastContentId());
        int maxUserId = Math.toIntExact(memberRepository.findLastUserId());

        //유저 X 콘텐츠 맵 만들기
        List<Post> postsList = shareRepository.findSharedPost();
        Map<Integer, BitSet> usersByContents = new HashMap<>();
        for(Post post: postsList){
            int memberId = Math.toIntExact(post.getMember().getId());
            int contentId = Math.toIntExact(post.getContents().getId());
            if(!usersByContents.containsKey(memberId)){
                usersByContents.put(memberId, new BitSet(maxContentId));
            }
            usersByContents.get(memberId).set(contentId);
        }

        //유저당 점수 구하기
        BitSet A = usersByContents.get((int)userId);
        //2. 대상 id와 불러온 id (팔로우 된 id는 제외) 들의 코사인 유사도 계산
        for(int key : usersByContents.keySet()){
            if(key==userId) continue;
            scoreByUserMap.put(key, cosineSimilarity(A, usersByContents.get(key)));
        }

        //콘텐츠당 점수 구하기

        //콘텐츠 X 유저 맵 만들기
        Map<Integer, BitSet> contentsByUsers = new HashMap<>();
        for(Post post:postsList){
            int memberId = Math.toIntExact(post.getMember().getId());
            int contentId = Math.toIntExact(post.getContents().getId());
            if(!contentsByUsers.containsKey(contentId)) {
                contentsByUsers.put(contentId, new BitSet(maxUserId));
            }
            contentsByUsers.get(contentId).set(memberId);
        }

        //타겟 유저가 작성한 포스트 만큼 반복
        BitSet postsByTargetUser = contentsByUsers.get((int)userId);
        int contentId = postsByTargetUser.nextSetBit(0);
        for(int i=0; i<postsByTargetUser.cardinality(); i++){
            BitSet B = contentsByUsers.get(contentId);
            //코사인유사도 계산
            for(Integer key : contentsByUsers.keySet()){
                double score = cosineSimilarity(B, contentsByUsers.get(key));
                if(scoreByContentMap.containsKey(key)) scoreByContentMap.put(key, scoreByContentMap.get(key)+score);
                else scoreByContentMap.put(key, score);
            }
            contentId = postsByTargetUser.nextSetBit(contentId);
        }

        int size = postsByTargetUser.cardinality();
        for(int key : scoreByContentMap.keySet()){
            int divider = size;
            if(postsByTargetUser.get(key)) divider--;
            scoreByContentMap.put(key, scoreByContentMap.get(key)/divider);
        }

        //3. arrayList 점수 순으로 정렬 해서 반환

        //Share 객체와 score 함께 저장할 arrayList
        List<Share> shares = shareRepository.findAll();
        ArrayList<ScoredShare> scoredShares = new ArrayList<>();
        for(Share share : shares){
            Post post = postRepository.findById(share.getPostId()).get();
            if(post.getMember().getId()==userId) continue;
            ScoredShare scoredShare = new ScoredShare(share, (scoreByUserMap.get(post.getMember().getId().intValue())+scoreByContentMap.get(post.getContents().getId().intValue()))/2);
            scoredShares.add(scoredShare);
        }

        scoredShares.sort(Comparator.comparingDouble(ScoredShare::getScore).reversed());
        for(ScoredShare scoredShare : scoredShares ){
            recommendedShares.add(scoredShare.getShare());
        }

        return recommendedShares;

    }


}