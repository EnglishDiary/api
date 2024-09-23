package org.eng_diary.api.business.expression.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ExpressionRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ExpressionRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }



}
