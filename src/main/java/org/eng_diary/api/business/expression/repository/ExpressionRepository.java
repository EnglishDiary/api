package org.eng_diary.api.business.expression.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.expression.entity.Composition;
import org.eng_diary.api.business.expression.entity.Expression;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.business.auth.entity.QMember.member;
import static org.eng_diary.api.business.expression.entity.QComposition.composition;
import static org.eng_diary.api.business.expression.entity.QExpression.expression;


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

    public List<Expression> getExpressionList(Pageable pageable) {
        return queryFactory.selectFrom(expression)
                .join(expression.member, member).fetchJoin()
                .join(expression.compositions, composition).fetchJoin()
                .orderBy(expression.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public JPAQuery<Long> getExpressionCountQuery() {
        return queryFactory.select(expression.count())
                .from(expression);
    }


}
