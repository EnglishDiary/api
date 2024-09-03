package org.eng_diary.api.business.diary.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.service.DiaryService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping("/ai-correction")
    public ResponseEntity<ApiResponse<String>> requestAICorrecion() {
        String result = diaryService.requestAICorrection();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

}
