package org.eng_diary.api.business.exam.repository;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.exam.payload.ExamSentencesRequest;
import org.eng_diary.api.domain.ExamSentence;
import org.eng_diary.api.domain.ExamSet;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.QExamSentence.examSentence;
import static org.eng_diary.api.domain.QExamSet.examSet;

@Repository
public class ExamRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ExamRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public ExamSet findExamSet(ExamSentencesRequest request) {
        return queryFactory.selectFrom(examSet)
                .where(examSet.topic.id.eq(request.getTopicId()))
                .fetchFirst();
    }

    public List<ExamSentence> findSentences(ExamSentencesRequest request) {
        return queryFactory.selectFrom(examSentence)
                .where(examSentence.level.eq(request.getLevel().getCode())
//                        .and(examSentence.examSet.id.eq(4L))
                        .and(examSentence.examTopic.id.eq(request.getTopicId()))
                )
                .orderBy(Expressions.numberTemplate(Double.class, "RANDOM()").asc())
                .limit(request.getSentenceCount())
                .fetch();
    }
}
