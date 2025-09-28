package org.eng_diary.api.business.test;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.eng_diary.api.business.test.entity.Mapping;
import org.eng_diary.api.business.test.entity.Server;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.eng_diary.api.business.test.entity.QGroup.group;
import static org.eng_diary.api.business.test.entity.QMapping.mapping;
import static org.eng_diary.api.business.test.entity.QServer.server;

@Repository
public class TestQueryRepository {

    private final JPAQueryFactory queryFactory;

    public TestQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Server> findServer() {
        return queryFactory.selectFrom(server)
                .innerJoin(server.mappings, mapping).fetchJoin()  // innerJoin으로 변경
                .innerJoin(mapping.group, group).fetchJoin()
                .fetch();
    }

    public List<Mapping> findMapping() {
        return queryFactory.selectFrom(mapping)
                .innerJoin(mapping.server, server).fetchJoin()
                .innerJoin(mapping.group, group).fetchJoin()
                .fetch();
    }


}
