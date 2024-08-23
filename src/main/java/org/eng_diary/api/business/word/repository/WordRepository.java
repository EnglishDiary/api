package org.eng_diary.api.business.word.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.QMember.member;
import static org.eng_diary.api.domain.QMemberWord.memberWord;
import static org.eng_diary.api.domain.QMemberWordCategory.memberWordCategory;
import static org.eng_diary.api.domain.QMemberWordExample.memberWordExample;
import static org.eng_diary.api.domain.QMemberWordKind.memberWordKind;
import static org.eng_diary.api.domain.QMemberWordMeaning.memberWordMeaning;
import static org.eng_diary.api.domain.QWordOriginalData.*;

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

        return queryFactory.selectFrom(wordOriginalData)
                .where(wordOriginalData.wordTitle.eq(word))
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

    public List<MemberWord> findMemberWords(Long memberId) {
        return queryFactory.selectFrom(memberWord)
                .join(memberWord.kinds, memberWordKind).fetchJoin()
                .where(memberWord.member.id.eq(memberId))
                .fetch();
    }

    public List<MemberWordMeaning> findMeanings(MemberWordKind kind) {
        return queryFactory.selectFrom(memberWordMeaning)
                .leftJoin(memberWordMeaning.examples, memberWordExample).fetchJoin()
                .where(memberWordMeaning.kind.eq(kind))
                .fetch();

    }

    public MemberWord findMemberWord(Long wordId, Long memberId) {
        return queryFactory.selectFrom(memberWord)
                .where(memberWord.member.id.eq(memberId)
                        .and(memberWord.id.eq(wordId)))
                .fetchOne();

    }

    public void deleteMemberWord(MemberWord memberWord) {
        em.remove(memberWord);
    }

    public List<MemberWordCategory> findMemberCategories(Long memberId) {
        return queryFactory.selectFrom(memberWordCategory)
                .where(memberWordCategory.categoryOwnerId.eq(memberId))
                .fetch();
    }

    public List<MemberWord> findMemberWordsByCategory(Long memberId, Long categoryId) {
        return queryFactory.selectFrom(memberWord)
                .join(memberWord.kinds, memberWordKind).fetchJoin()
                .where(memberWord.member.id.eq(memberId)
                        .and(memberWord.memberWordCategory.id.eq(categoryId)))
                .fetch();
    }
}
