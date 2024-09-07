package org.eng_diary.api.business.diary.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.AICorrectionRequest;
import org.eng_diary.api.business.diary.dto.DiaryDTO;
import org.eng_diary.api.business.diary.dto.DiarySaveRequest;
import org.eng_diary.api.business.diary.dto.OfficialCategoryDTO;
import org.eng_diary.api.business.diary.service.DiaryService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/category/official")
    public ResponseEntity<ApiResponse<List<OfficialCategoryDTO>>> getOfficialCategory() {
        return ResponseEntity.ok(ApiResponse.success(diaryService.getOfficialCategory()));
    }

    @PostMapping("/ai-correction")
    public ResponseEntity<ApiResponse<Map<String, String>>> requestAICorrection(@RequestBody AICorrectionRequest aiCorrectionRequest) {
        String diary = aiCorrectionRequest.getDiary();

        return ResponseEntity.ok(ApiResponse.success(diaryService.requestAICorrection(diary)));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveDiary(@RequestBody DiarySaveRequest diarySaveRequest) {
        diaryService.saveDiary(diarySaveRequest);
        return ResponseEntity.ok(ApiResponse.success("다이어리 업로드 성공", null));
    }

    @GetMapping("/official-category/{categoryId}/list")
    public ResponseEntity<ApiResponse<?>> getDiaries(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(diaryService.getDiaries(categoryId)));
    }

}
