package org.eng_diary.api.business.study.repository;

import org.eng_diary.api.business.study.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopicRepository extends JpaRepository<Topic, Long> {
}
