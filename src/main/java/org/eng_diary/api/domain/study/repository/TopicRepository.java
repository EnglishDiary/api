package org.eng_diary.api.domain.study.repository;

import org.eng_diary.api.domain.study.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
