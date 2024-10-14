package org.eng_diary.api.business.diary.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.*;
import org.eng_diary.api.business.diary.service.DiaryService;
import org.eng_diary.api.business.filemng.service.FileManagerService;
import org.eng_diary.api.dto.ApiResponse;
import org.eng_diary.api.security.CurrentUser;
import org.eng_diary.api.security.UserPrincipal;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @GetMapping("/category/official")
    public ResponseEntity<ApiResponse<List<OfficialCategoryDTO>>> getOfficialCategory() {
        return ApiResponse.success(diaryService.getOfficialCategory());
    }

    @PostMapping("/ai-correction")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestAICorrection(@RequestBody AICorrectionRequest aiCorrectionRequest) {
        String diary = aiCorrectionRequest.getDiary();

        return ApiResponse.success(diaryService.requestAICorrection(diary));
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<?>> saveDiary(@CurrentUser UserPrincipal userPrincipal, @RequestPart(value = "file", required = false ) MultipartFile file, @Valid @RequestPart(value = "jsonData")  DiarySaveRequest diarySaveRequest) {
        diaryService.saveDiary(diarySaveRequest, userPrincipal.getId(), file);
        return ApiResponse.success("다이어리 업로드 성공");
    }

    @GetMapping("/official-category/{categoryId}/list")
    public ResponseEntity<ApiResponse<List<DiaryDTO>>> getDiaries(
            @PathVariable("categoryId") Long categoryId,
//            @ModelAttribute PaginationData paginationData
//            @ModelAttribute DiaryListRequest diaryListRequest,
            Pageable pageable) {
        return ApiResponse.successWithPaging(diaryService.getDiaries(categoryId, pageable));
    }

    @GetMapping("/{diaryId}/detail")
    public ResponseEntity<ApiResponse<DiaryDetailDTO>> getDiaryDetail(@PathVariable("diaryId") Long diaryId) {
        return ApiResponse.success(diaryService.getDiaryDetail(diaryId));
    }

}
