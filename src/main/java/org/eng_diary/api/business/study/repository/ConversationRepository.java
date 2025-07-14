package org.eng_diary.api.business.study.repository;

import org.eng_diary.api.business.study.entity.Conversation;
import org.eng_diary.api.business.study.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findAllBySentence(Sentence sentence);
}
