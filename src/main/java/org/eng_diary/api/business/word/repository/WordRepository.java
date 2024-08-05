package org.eng_diary.api.business.word.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.Member;
import org.eng_diary.api.domain.QMember;
import org.springframework.stereotype.Repository;

import static org.eng_diary.api.domain.QMember.*;
import static org.eng_diary.api.domain.QMember.member;

@Repository
public class WordRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public WordRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Member findMember() {
        return em.find(Member.class, 1L);
    }

    public Member findMember2() {

        return queryFactory.selectFrom(member)
                .where(member.name.eq("최상욱"))
                .where(member.id.eq(1L))
                .fetchOne();
    }

}
