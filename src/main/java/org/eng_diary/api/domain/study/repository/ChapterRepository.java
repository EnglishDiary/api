package org.eng_diary.api.domain.study.repository;

import org.eng_diary.api.domain.study.entity.Chapter;
import org.eng_diary.api.domain.study.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {


}
