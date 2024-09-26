package org.eng_diary.api.business.expression.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.AICorrectionRequest;
import org.eng_diary.api.business.expression.payload.CompositionRequest;
import org.eng_diary.api.business.expression.payload.ExpressionDTO;
import org.eng_diary.api.business.expression.payload.ExpressionSaveRequest;
import org.eng_diary.api.business.expression.service.ExpressionService;
import org.eng_diary.api.dto.ApiResponse;
import org.eng_diary.api.security.CurrentUser;
import org.eng_diary.api.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/expression")
@RequiredArgsConstructor
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping("/ai-correction")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestAICorrection(@RequestBody @Valid CompositionRequest request) {
        return ApiResponse.success(expressionService.requestAICorrection(request));
    }

    @PostMapping("/save")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiResponse<?>> saveExpression(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ExpressionSaveRequest request) {
        expressionService.saveExpression(request, userPrincipal.getId());
        return ApiResponse.success("표현 업로드 성공");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ExpressionDTO>>> getExpressionList() {
        return ApiResponse.success(expressionService.getExpressionList());
    }

}
