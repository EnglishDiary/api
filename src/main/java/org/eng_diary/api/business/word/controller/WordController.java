package org.eng_diary.api.business.word.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.word.dto.*;
import org.eng_diary.api.business.word.service.WordService;
import org.eng_diary.api.dto.ApiResponse;
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
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/test/multiple")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> testMultipleData() {
        List<MemberResponse> memberResponses = wordService.testMultipleData();
        return ResponseEntity.ok(ApiResponse.success(memberResponses));
    }

    @GetMapping("/{word}")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> findWord(@PathVariable("word") String word) {
        List<Map<String, Object>> wordInfo = wordService.createWordInfo(word);
        return ResponseEntity.ok(ApiResponse.success(wordInfo));
    }

    @PostMapping("/save/{word}")
    public ResponseEntity<ApiResponse<?>> saveWord(@PathVariable("word") String word, @RequestBody WordSaveRequest wordSaveRequest) {
        wordService.saveWordInfo(word, wordSaveRequest.getJsonData());

        // TODO 240823 ok말고 created 써보기
        return ResponseEntity.ok(ApiResponse.success("단어 업로드 성공", null));
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<String>> getMemberWords() {
        String jsonString = wordService.getMemberWords(0L); // TODO 240823 상수화

        return ResponseEntity.ok(ApiResponse.success(jsonString));
    }

    @DeleteMapping("/delete/{word}")
    public ResponseEntity<ApiResponse<?>> deleteWord(@RequestBody WordDeleteRequest wordDeleteRequest) {
        wordService.deleteMemberWord(wordDeleteRequest.getWordId());

        return ResponseEntity.ok(ApiResponse.success("단어 삭제 성공", null));
    }

    @PostMapping("/update/{word}")
    public ResponseEntity<ApiResponse<?>> updateWord(@RequestBody WordUpdateRequest wordUpdateRequest) {
        wordService.updateMemberWord(wordUpdateRequest);

        return ResponseEntity.ok(ApiResponse.success("단어 업데이트 성공", null));
    }

    @GetMapping("/category")
    public ResponseEntity<ApiResponse<List<MemberWordCategoryResponse>>> getMemberCategories() {
        return ResponseEntity.ok(ApiResponse.success(wordService.getMemberCategories()));
    }

    @GetMapping("/{categoryId}/list")
    public ResponseEntity getMemberWordsByCategory(@PathVariable("categoryId") Long categoryId) {
        String jsonString = wordService.getMemberWords(categoryId);

        return ResponseEntity.ok(ApiResponse.success(jsonString));
    }


}
