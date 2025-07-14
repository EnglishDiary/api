package org.eng_diary.api.domain.auth.repository;

import org.eng_diary.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginId);
}
