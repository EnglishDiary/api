package org.eng_diary.api.business.member.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.member.dto.LoginRequest;
import org.eng_diary.api.business.member.dto.SignupRequest;
import org.eng_diary.api.business.member.service.MemberService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Long>> signup(@RequestBody @Valid SignupRequest request) {
        Long memberId = memberService.signup(request);
        return ApiResponse.test("회원가입 성공", memberId);
//        return ResponseEntity.ok(ApiResponse.success("회원가입에 성공하였습니다.", memberId));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {

    }



}
