package org.eng_diary.api.business.expression.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.expression.entity.Job;
import org.eng_diary.api.business.expression.entity.Task;
import org.eng_diary.api.domain.Composition;
import org.eng_diary.api.domain.Expression;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.business.expression.entity.QAttachFile.attachFile;
import static org.eng_diary.api.business.expression.entity.QJob.job;
import static org.eng_diary.api.business.expression.entity.QSvr.svr;
import static org.eng_diary.api.business.expression.entity.QTask.task;
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

    public List<Expression> getExpressionList(Pageable pageable) {
        return queryFactory.selectFrom(expression)
                .join(expression.member, member).fetchJoin()
                .join(expression.compositions, composition).fetchJoin()
                .orderBy(expression.registerTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public JPAQuery<Long> getExpressionCountQuery() {
        return queryFactory.select(expression.count())
                .from(expression);
    }

    public void insertTask(Task task) {
        em.persist(task);
    }

    public Job findJob() {
        return queryFactory.selectFrom(job)
                .join(job.tasks, task).fetchJoin()
                .leftJoin(task.svrs, svr).fetchJoin()
                .where(job.id.eq(1L))
                .fetchOne();
    }

    public Job findJobById(Long jobId) {
        return queryFactory.selectFrom(job)
                .innerJoin(job.tasks, task).fetchJoin()
//                .leftJoin(task.svrs, svr)
//                .leftJoin(task.files, attachFile)
                .where(task.job.id.eq(jobId))
                .fetchOne();
    }
}
