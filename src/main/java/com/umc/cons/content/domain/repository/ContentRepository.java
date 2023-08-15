package com.umc.cons.content.domain.repository;

import com.umc.cons.content.domain.entity.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContentRepository {

    private final EntityManager em;

    public void save(Content content){
        em.persist(content);
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

    public Long findLastContentId(){
        return (Long) em.createQuery("select max(c.id) from Content c").getResultList().get(0);
    }

}
