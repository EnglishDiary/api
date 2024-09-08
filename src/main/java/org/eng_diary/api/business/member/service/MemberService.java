package org.eng_diary.api.business.member.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.member.dto.SignupRequest;
import org.eng_diary.api.business.member.repository.MemberRepository;
import org.eng_diary.api.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long signup(SignupRequest request) {
        Member member = new Member();

        member.setName(request.getNickname());
        member.setRegistrationId(request.getUserId());
        member.setProfileImageUrl(request.getProfileImageUrl());

        memberRepository.saveMember(member);

        return member.getId();
    }
}
