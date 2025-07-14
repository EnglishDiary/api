package org.eng_diary.api.business.member.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.member.payload.CategoryUpdateRequest;
import org.eng_diary.api.business.member.service.MemberService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/word/category/update")
    public ResponseEntity<ApiResponse<?>> updateWordCategory(@RequestBody CategoryUpdateRequest request) {
        // todo - id 조회 필요함
        memberService.updateWordCategory(request);
        return ApiResponse.success("카테고리 업데이트 성공");
    }


}
