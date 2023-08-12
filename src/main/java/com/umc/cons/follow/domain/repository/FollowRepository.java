package com.umc.cons.follow.domain.repository;

import com.umc.cons.content.domain.entity.Content;
import com.umc.cons.follow.domain.entity.Follow;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {
    private final EntityManager em;

    public void save(Follow follow){
        em.persist(follow);
    }
    public Content findOne(Long id){
        return em.find(Content.class, id);
    }

    public List<Content> findAll() {
        return em.createQuery("select m from Content m",Content.class).getResultList();
    }

    public List<Content> findByName(String name){
        return em.createQuery("select m from Content m where m.name = :name",Content.class).setParameter("name",name).getResultList();
    }

}
