package org.eng_diary.api.business.member.repository;

import org.eng_diary.api.business.auth.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
