package org.eng_diary.api.business.expression.repository;

import org.eng_diary.api.business.expression.entity.Job;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @EntityGraph(attributePaths = {"tasks", "tasks.svrs", "tasks.files"})
    Optional<Job> findById(Long id);

    @Query("SELECT j FROM Job j " +
            "INNER JOIN FETCH j.tasks t " +
            "LEFT JOIN FETCH t.svrs " +
            "LEFT JOIN FETCH t.files " +
            "WHERE j.id = :jobId")
    Optional<Job> findByIdWithTasksAndSvrs(@Param("jobId") Long jobId);

}
