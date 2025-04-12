package org.eng_diary.api.domain.member.repository;

import org.eng_diary.api.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
