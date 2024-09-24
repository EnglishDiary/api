package org.eng_diary.api.business.expression.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.Composition;
import org.eng_diary.api.domain.Expression;
import org.springframework.stereotype.Repository;

@Repository
public class ExpressionRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ExpressionRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public void saveExpression(Expression expression) {
        em.persist(expression);
    }

    public void saveComposition(Composition composition) {
        em.persist(composition);
    }
}
