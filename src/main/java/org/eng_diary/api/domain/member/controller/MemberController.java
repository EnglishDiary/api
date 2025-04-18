package org.eng_diary.api.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.member.dto.response.LoginRes;
import org.eng_diary.api.domain.member.dto.response.MemberResponse;
import org.eng_diary.api.domain.member.dto.request.LoginForm;
import org.eng_diary.api.domain.member.dto.request.SignupForm;
import org.eng_diary.api.domain.member.service.MemberService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Tag(name = "사용자 API", description = "사용자 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> signup(@RequestBody @Valid SignupForm signupForm) {
        MemberResponse memberResponse = memberService.signup(signupForm);
        return ApiResponse.success("회원가입에 성공하였습니다", memberResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@RequestBody LoginForm loginForm) {
        return ApiResponse.success(memberService.login(loginForm));
    }

}
