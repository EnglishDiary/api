package org.eng_diary.api.business.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.Member;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void saveMember(Member member) {
        em.persist(member);
    }
}
