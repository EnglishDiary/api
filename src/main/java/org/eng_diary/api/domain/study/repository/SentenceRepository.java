package org.eng_diary.api.domain.study.repository;

import org.eng_diary.api.domain.study.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
}
