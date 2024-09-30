package org.eng_diary.api.business.expression.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.Composition;
import org.eng_diary.api.domain.Expression;
import org.eng_diary.api.domain.QExpression;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.QComposition.composition;
import static org.eng_diary.api.domain.QExpression.expression;
import static org.eng_diary.api.domain.QMember.member;

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

    public List<Expression> getExpressionList() {
        return queryFactory.selectFrom(expression)
                .join(expression.member, member).fetchJoin()
                .join(expression.compositions, composition).fetchJoin()
                .orderBy(expression.registerTime.desc())
                .fetch();
    }
}
