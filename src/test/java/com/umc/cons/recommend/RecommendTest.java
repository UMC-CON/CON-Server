package com.umc.cons.recommend;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.content.domain.repository.ContentRepository;
import com.umc.cons.member.domain.entity.Member;
import com.umc.cons.member.domain.repository.MemberRepository;
import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.post.domain.repository.PostRepository;
import com.umc.cons.share.domain.entity.Share;
import com.umc.cons.share.domain.repository.ShareRepository;
import com.umc.cons.share.service.ShareService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class RecommendTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private ShareService shareService;

    @Test
    @DisplayName("실행 시간을 측정")
    void recommendedDurationTest() {

        System.out.println("asd");
        //saveDemoData(0, 500, 500, 50, 5000);

        int userTot = 5000;
        int contentTot = 5000;
        int user1PostTot = 50;
        int postTot = 50000 + user1PostTot;

        //임의의 유저 저장
        Member[] members = new Member[userTot];
        for (int i = 0; i < members.length; i++) {
            members[i] = Member.builder().name("user" + i).build();
            memberRepository.save(members[i]);
        }

        //임의의 콘텐츠 저장
        Content[] contents = new Content[contentTot];
        for (int i = 0; i < contents.length; i++) {
            contents[i] = new Content();
            contents[i].setName(""+i);
            contentRepository.save(contents[i]);
        }

        //임의의 포스트 저장
        Post[] posts = new Post[postTot];
        for (int i = 0; i < posts.length - user1PostTot; i++) {
            //posts[i] = new Post(members[i % userTot].getId(), contents[i % contentTot].getId());
            posts[i] = new Post();
            posts[i].setMemberId(members[i % userTot].getId());
            posts[i].setContentId(contents[i % contentTot].getId());
            postRepository.save(posts[i]);
        }
        //유저1이 작성한 포스트 저장
        for (int i = 0; i < user1PostTot; i++) {
            Post post = new Post();
            post.setMemberId(members[0].getId());
            post.setContentId(contents[i].getId());
            posts[postTot - user1PostTot + i] = post;
            postRepository.save(posts[postTot - user1PostTot + i]);
        }

        //공유 저장
        Share[] shares = new Share[postTot];
        for (int i = 0; i < shares.length; i++) {
            Share share = new Share();
            share.setPostId(posts[i].getId());
            shares[i] = share;
            shareRepository.save(shares[i]);
        }

        long startTime = System.currentTimeMillis();
        shareService.recommendedShares(members[0].getId());
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        System.out.println("executionTime: " + executionTime + "ms");
    }
}
