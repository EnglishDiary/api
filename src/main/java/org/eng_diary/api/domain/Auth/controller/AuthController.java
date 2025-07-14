package org.eng_diary.api.domain.Auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.Auth.dto.response.LoginRes;
import org.eng_diary.api.domain.Auth.dto.response.MemberResponse;
import org.eng_diary.api.domain.Auth.dto.request.LoginForm;
import org.eng_diary.api.domain.Auth.dto.request.SignupForm;
import org.eng_diary.api.domain.Auth.service.AuthService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "사용자 API", description = "사용자 관련 API")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<MemberResponse>> signup(@RequestBody @Valid SignupForm signupForm) {
        MemberResponse memberResponse = authService.signup(signupForm);
        return ApiResponse.success("회원가입에 성공하였습니다", memberResponse);
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@RequestBody LoginForm loginForm) {
        return ApiResponse.success(authService.login(loginForm));
    }

    @Operation(summary = "내 정보 조회")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> identifyUser() {
        return ApiResponse.success(authService.identifyUser());
    }

}
