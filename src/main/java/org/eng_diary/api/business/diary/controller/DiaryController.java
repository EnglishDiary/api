package org.eng_diary.api.business.diary.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.*;
import org.eng_diary.api.business.diary.service.DiaryService;
import org.eng_diary.api.common.dto.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/category/official")
    public ResponseEntity<ApiResponse<List<OfficialCategoryDTO>>> getOfficialCategory() {
        return ApiResponse.success(diaryService.getOfficialCategory());
    }

    @PostMapping("/ai-correction")
    public ResponseEntity<ApiResponse<Map<String, String>>> requestAICorrection(@RequestBody AICorrectionRequest aiCorrectionRequest) {
        String diary = aiCorrectionRequest.getDiary();

        return ApiResponse.success(diaryService.requestAICorrection(diary));
    }

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> saveDiary(
            @RequestPart("jsonData") DiarySaveRequest diarySaveRequest,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        diaryService.saveDiary(diarySaveRequest, file);
        return ApiResponse.success("다이어리 업로드 성공");
    }

    @GetMapping("/official-category/{categoryId}/list")
    public ResponseEntity<ApiResponse<List<DiaryDTO>>> getDiaries(@PathVariable("categoryId") Long categoryId) {
        return ApiResponse.success(diaryService.getDiaries(categoryId));
    }

    @GetMapping("/{diaryId}/detail")
    public ResponseEntity<ApiResponse<DiaryDetailDTO>> getDiaryDetail(@PathVariable("diaryId") Long diaryId) {
        return ApiResponse.success(diaryService.getDiaryDetail(diaryId));
    }

//    @GetMapping("/{diaryId}/detail")
//    public ResponseEntity<DiaryDetailDTO> getDiaryDetail(@PathVariable("diaryId") Long diaryId) {
//        return ResponseEntity.ok(diaryService.getDiaryDetail(diaryId));
//    }

}
