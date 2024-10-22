package org.eng_diary.api.business.exam.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.exam.payload.ExamSentenceDTO;
import org.eng_diary.api.business.exam.payload.ExamSentencesRequest;
import org.eng_diary.api.business.exam.service.ExamService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @GetMapping("/sentences")
    public ResponseEntity<ApiResponse<List<ExamSentenceDTO>>> getSentences(ExamSentencesRequest request) {
        return ApiResponse.success(examService.getSentences(request));
    }

}
