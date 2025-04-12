package org.eng_diary.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.member.dto.response.MemberResponse;
import org.eng_diary.api.domain.member.dto.request.LoginForm;
import org.eng_diary.api.domain.member.dto.request.SignupForm;
import org.eng_diary.api.domain.member.mapper.MemberMapper;
import org.eng_diary.api.domain.member.repository.MemberRepository;
import org.eng_diary.api.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponse signup(SignupForm signupForm) {
        Member member = MemberMapper.createMember(signupForm);
        memberRepository.save(member);

        return MemberMapper.createMemberResponse(member);
    }

    public MemberResponse login(LoginForm loginForm) {

        return null;
    }
}
