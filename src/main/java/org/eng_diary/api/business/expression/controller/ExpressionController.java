package org.eng_diary.api.business.expression.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.AICorrectionRequest;
import org.eng_diary.api.business.expression.payload.CompositionRequest;
import org.eng_diary.api.business.expression.service.ExpressionService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/expression")
@RequiredArgsConstructor
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping("/ai-correction")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestAICorrection(@RequestBody @Valid CompositionRequest request) {
        return ApiResponse.success(expressionService.requestAICorrection(request));
    }

}
