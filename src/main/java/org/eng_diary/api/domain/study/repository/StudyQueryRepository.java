package org.eng_diary.api.domain.study.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.study.entity.Chapter;
import org.eng_diary.api.domain.study.entity.Script;
import org.eng_diary.api.domain.study.entity.Topic;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.study.entity.QChapter.chapter;
import static org.eng_diary.api.domain.study.entity.QScript.script;
import static org.eng_diary.api.domain.study.entity.QSentence.sentence;
import static org.eng_diary.api.domain.study.entity.QTopic.topic;

@Repository
public class StudyQueryRepository {
    private final JPAQueryFactory queryFactory;

    public StudyQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Topic> findTopics(Long userId) {
        return queryFactory.selectFrom(topic)
                .where(topic.member.id.eq(userId))
                .fetch();
    }

    public List<Chapter> findChapters(Long topicId) {
        return queryFactory.selectFrom(chapter)
                .where(chapter.topic.id.eq(topicId))
                .fetch();
    }

    public Script findScript(Long scriptId) {
        return queryFactory.selectFrom(script)
                .join(script.sentences, sentence).fetchJoin()
                .where(script.id.eq(scriptId))
                .fetchOne();
    }
}
