package com.umc.cons.share.domain.repository;

import com.umc.cons.post.domain.entity.Post;
import com.umc.cons.share.domain.entity.Share;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShareRepository extends JpaRepository<Share, Long> {
    Page<Share> findAllBy(Pageable pageable);

    @Query("select p from post p where p.id in (select s.postId from share s) ")
    List<Post> findSharedPost();

}