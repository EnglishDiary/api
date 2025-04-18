package org.eng_diary.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.common.util.JwtTokenUtil;
import org.eng_diary.api.domain.member.dto.response.LoginRes;
import org.eng_diary.api.domain.member.dto.response.MemberResponse;
import org.eng_diary.api.domain.member.dto.request.LoginForm;
import org.eng_diary.api.domain.member.dto.request.SignupForm;
import org.eng_diary.api.domain.member.mapper.MemberMapper;
import org.eng_diary.api.domain.member.repository.MemberRepository;
import org.eng_diary.api.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public MemberResponse signup(SignupForm signupForm) {
        Member member = MemberMapper.createMember(signupForm);
        memberRepository.save(member);

        return MemberMapper.createMemberResponse(member);
    }

    public LoginRes login(LoginForm loginForm) {
        String userId = loginForm.memberId();
        System.out.println(userId);

        Map<String, Object> claims = new HashMap<>();
        String token = jwtTokenUtil.generateToken(claims, userId);

        System.out.println(token);

        return LoginRes.builder()
                .jwt(token)
                .build();
    }
}
