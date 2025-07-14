package org.eng_diary.api.business.study.repository;

import org.eng_diary.api.business.study.entity.Sentence;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SentenceRepository extends JpaRepository<Sentence, Long> {
    default Sentence findByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new RuntimeException(""));
    }

}
