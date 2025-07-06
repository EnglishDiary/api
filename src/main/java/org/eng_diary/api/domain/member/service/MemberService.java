package org.eng_diary.api.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.eng_diary.api.common.context.UserContext;
import org.eng_diary.api.common.context.UserContextHolder;
import org.eng_diary.api.common.util.JwtTokenUtil;
import org.eng_diary.api.domain.member.dto.response.LoginRes;
import org.eng_diary.api.domain.member.dto.response.MemberResponse;
import org.eng_diary.api.domain.member.dto.request.LoginForm;
import org.eng_diary.api.domain.member.dto.request.SignupForm;
import org.eng_diary.api.domain.member.mapper.MemberMapper;
import org.eng_diary.api.domain.member.repository.MemberRepository;
import org.eng_diary.api.entity.Member;
import org.eng_diary.api.exception.customError.BadRequestError;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse signup(SignupForm signupForm) {
        String encodedPassword = passwordEncoder.encode(signupForm.getPassword());

        Member member = MemberMapper.createMember(signupForm, encodedPassword);
        memberRepository.save(member);

        return MemberMapper.createMemberResponse(member);
    }

    public LoginRes login(LoginForm loginForm) {
        String userId = loginForm.loginId();
        Member user = memberRepository.findByLoginId(userId);

        if (user == null) {
            throw new BadRequestError("not existed user");
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginForm.password(), user.getPassword())) {
            throw new RuntimeException("password is incorrect");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", user.getId());

        String token = jwtTokenUtil.generateToken(claims, userId);

        return LoginRes.builder()
                .accessToken(token)
                .build();
    }

    public MemberResponse identifyUser() {
        UserContext userContext = UserContextHolder.getUserContext();

        Member user = memberRepository.findByLoginId(userContext.loginId());
        return MemberMapper.createMemberResponse(user);
    }

    public Member getCurrentUser() {
        UserContext userContext = UserContextHolder.getUserContext();

        return memberRepository.findById(userContext.memberId())
                .orElseThrow(() -> new RuntimeException("not existed user"));
    }
}
