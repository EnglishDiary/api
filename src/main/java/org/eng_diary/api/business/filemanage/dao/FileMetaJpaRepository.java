package org.eng_diary.api.business.filemanage.dao;

import org.eng_diary.api.business.filemanage.entity.EntityFileRelation;
import org.eng_diary.api.business.filemanage.entity.FileMeta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileMetaJpaRepository extends JpaRepository<FileMeta, Long> {
    List<FileMeta> findByEntityFileRelation(EntityFileRelation entityFileRelation);
}
