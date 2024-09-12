package org.eng_diary.api.business.word.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.word.dto.*;
import org.eng_diary.api.business.word.service.WordService;
import org.eng_diary.api.dto.ApiResponse;
import org.eng_diary.api.security.CurrentUser;
import org.eng_diary.api.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/test")
    public ResponseEntity<ApiResponse<MemberResponse>> test() {
        MemberResponse result = wordService.testService();
        return ApiResponse.success(result);
    }

    @GetMapping("/test/multiple")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> testMultipleData() {
        List<MemberResponse> memberResponses = wordService.testMultipleData();
        return ApiResponse.success(memberResponses);
    }

    @GetMapping("/{word}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> findWord(@PathVariable("word") String word) {
        List<Map<String, Object>> wordInfo = wordService.createWordInfo(word);
        return ApiResponse.success(wordInfo);
    }

    @PostMapping("/save/{word}")
    public ResponseEntity<ApiResponse<?>> saveWord(@CurrentUser UserPrincipal userPrincipal, @PathVariable("word") String word, @RequestBody WordSaveRequest wordSaveRequest) {
        wordService.saveWordInfo(word, wordSaveRequest, userPrincipal.getId());

        return ApiResponse.success("단어 업로드 성공");
    }

    @DeleteMapping("/delete/{word}")
    public ResponseEntity<ApiResponse<?>> deleteWord(@CurrentUser UserPrincipal userPrincipal, @RequestBody WordDeleteRequest wordDeleteRequest) {
        wordService.deleteMemberWord(wordDeleteRequest.getWordId(), userPrincipal.getId());

        return ApiResponse.success("단어 삭제 성공");
    }

    @PostMapping("/update/{word}")
    public ResponseEntity<ApiResponse<?>> updateWord(@CurrentUser UserPrincipal userPrincipal, @RequestBody WordUpdateRequest wordUpdateRequest) {
        wordService.updateMemberWord(wordUpdateRequest, userPrincipal.getId());

        return ApiResponse.success("단어 업데이트 성공");
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<MemberWordCategoryResponse>>> getMemberCategories(@CurrentUser UserPrincipal userPrincipal) {
        Long memberId = userPrincipal.getId();
        return ApiResponse.success(wordService.getMemberCategories(memberId));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<MemberWordDTO>> getMemberWords(@CurrentUser UserPrincipal userPrincipal) {
        MemberWordDTO memberWords = wordService.getMemberWords(0L, userPrincipal.getId());// TODO 240823 0L(전체 카테고리) 상수화

        return ApiResponse.success(memberWords);
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity<ApiResponse<MemberWordDTO>> getMemberWords(@CurrentUser UserPrincipal userPrincipal, @PathVariable("categoryId") Long categoryId) {
        MemberWordDTO memberWords = wordService.getMemberWords(categoryId, userPrincipal.getId());

        return ApiResponse.success(memberWords);
    }

}
