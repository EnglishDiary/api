package org.eng_diary.api.business.word.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.service.WordService;
import org.eng_diary.api.dto.ApiResponse;
import org.eng_diary.api.dto.WordSaveRequest;
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

        return ResponseEntity.ok(ApiResponse.success("단어 업로드 성공", null));
    }

}
