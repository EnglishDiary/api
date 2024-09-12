package org.eng_diary.api.business.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.member.payload.CategoryData;
import org.eng_diary.api.business.member.payload.CategoryUpdateRequest;
import org.eng_diary.api.domain.MemberWordCategory;
import org.springframework.stereotype.Repository;

import static org.eng_diary.api.domain.QMemberWordCategory.memberWordCategory;

@Repository
public class MemberRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public MemberRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public void addWordNewCategory(MemberWordCategory category) {
        em.persist(category);
    }

    public void updateWordCategory(CategoryData request, Long memberId) {
        queryFactory.update(memberWordCategory)
                .set(memberWordCategory.categoryName, request.getName())
                .where(memberWordCategory.id.eq(request.getId())
                    .and(memberWordCategory.member.id.eq(memberId)))
                .execute();
    }

    public void deleteWordCategory(Long categoryId, Long memberId) {
        queryFactory.delete(memberWordCategory)
                .where(memberWordCategory.id.eq(categoryId)
                        .and(memberWordCategory.member.id.eq(memberId)))
                .execute();
    }
}
