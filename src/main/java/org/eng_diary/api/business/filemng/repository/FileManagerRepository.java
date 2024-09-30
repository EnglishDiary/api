package org.eng_diary.api.business.filemng.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.domain.FileMeta;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.domain.QFileMeta.fileMeta;

@Repository
public class FileManagerRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public FileManagerRepository(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    public List<FileMeta> findFileMetaList(String tableName, List<Long> ids) {
        return queryFactory.selectFrom(fileMeta)
                .where(fileMeta.referencedTable.eq(tableName))
                .where(fileMeta.tableRowId.in(ids))
                .fetch();
    }

    public void saveFileMeta(FileMeta fileMeta) {
        em.persist(fileMeta);
    }
}
