package org.eng_diary.api.business.filemanage.dao;

import org.eng_diary.api.business.filemanage.entity.EntityFileRelation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityFileRelationJpaRepository extends JpaRepository<EntityFileRelation, Long> {
}
