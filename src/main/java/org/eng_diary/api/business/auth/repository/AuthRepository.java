package org.eng_diary.api.business.auth.repository;

import org.eng_diary.api.business.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Member, Long> {

    Member findByLoginId(String loginId);
}
