package org.eng_diary.api.business.diary.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.diary.dto.DiaryDTO;
import org.eng_diary.api.domain.Diary;
import org.eng_diary.api.domain.OfficialDiaryCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.QDiary.diary;
import static org.eng_diary.api.domain.QMember.member;
import static org.eng_diary.api.domain.QOfficialDiaryCategory.officialDiaryCategory;

@Repository
public class DiaryRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DiaryRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<OfficialDiaryCategory> findOfficialCategory() {
        return queryFactory.selectFrom(officialDiaryCategory)
                .fetch();
    }

    public void saveDiary(Diary diary) {
        em.persist(diary);
    }

    public List<Diary> findDiariesByCategory(Long categoryId) {
        return queryFactory.selectFrom(diary)
                .where(diary.officialDiaryCategory.id.eq(categoryId))
                .join(diary.member, member)
                .fetch();
    }
}
