package org.eng_diary.api.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.common.context.UserContext;
import org.eng_diary.api.common.context.UserContextHolder;
import org.eng_diary.api.common.util.JwtTokenUtil;
import org.eng_diary.api.domain.auth.dto.response.LoginRes;
import org.eng_diary.api.domain.auth.dto.response.MemberResponse;
import org.eng_diary.api.domain.auth.dto.request.LoginForm;
import org.eng_diary.api.domain.auth.dto.request.SignupForm;
import org.eng_diary.api.domain.auth.mapper.MemberMapper;
import org.eng_diary.api.domain.auth.repository.AuthRepository;
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
public class AuthService {

    private final AuthRepository authRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public MemberResponse signup(SignupForm signupForm) {
        String encodedPassword = passwordEncoder.encode(signupForm.getPassword());

        Member member = MemberMapper.createMember(signupForm, encodedPassword);
        authRepository.save(member);

        return MemberMapper.createMemberResponse(member);
    }

    public LoginRes login(LoginForm loginForm) {
        String userId = loginForm.loginId();
        Member user = authRepository.findByLoginId(userId);

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

        Member user = authRepository.findByLoginId(userContext.loginId());
        return MemberMapper.createMemberResponse(user);
    }

    public Member getCurrentUser() {
        UserContext userContext = UserContextHolder.getUserContext();

        return authRepository.findById(userContext.memberId())
                .orElseThrow(() -> new RuntimeException("not existed user"));
    }
}
