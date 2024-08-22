package org.eng_diary.api.business.word.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.*;
import org.springframework.stereotype.Repository;

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

    public void saveOriginalData(String word, String jsonString) {
        WordOriginalData originalData = WordOriginalData.builder()
                .wordTitle(word)
                .jsonString(jsonString)
                .build();

        em.persist(originalData);
    }

    public WordOriginalData findExistedWord(String word) {

        return queryFactory.selectFrom(QWordOriginalData.wordOriginalData)
                .where(QWordOriginalData.wordOriginalData.wordTitle.eq(word))
                .fetchFirst();
    }

    public void saveMemberWord(MemberWord memberWord) {
        em.persist(memberWord);
    }

    public void saveMemberWordKind(MemberWordKind kind) {
        em.persist(kind);
    }

    public void saveMemberWordMeaning(MemberWordMeaning wordMeaning) {
        em.persist(wordMeaning);
    }

    public void saveMemberWordExample(MemberWordExample memberWordExample) {
        em.persist(memberWordExample);
    }
}
