package org.eng_diary.api.business.expression.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.expression.payload.CompositionRequest;
import org.eng_diary.api.business.expression.payload.ExpressionDTO;
import org.eng_diary.api.business.expression.payload.ExpressionSaveRequest;
import org.eng_diary.api.business.expression.service.ExpressionService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/expression")
@RequiredArgsConstructor
public class ExpressionController {

    private final ExpressionService expressionService;

    @PostMapping("/ai-correction")
    public ResponseEntity<ApiResponse<Map<String, Object>>> requestAICorrection(@RequestBody @Valid CompositionRequest request) {
        return ApiResponse.success(expressionService.requestAICorrection(request));
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse<?>> saveExpression(@Valid @RequestBody ExpressionSaveRequest request) {
        expressionService.saveExpression(request, 1L);
        return ApiResponse.success("표현 업로드 성공");
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<ExpressionDTO>>> getExpressionList(
//            ExpressionRequest request,
            Pageable pageable) {
//        return ApiResponse.success(expressionService.getExpressionList());
        return ApiResponse.successWithPaging(expressionService.getExpressionList(pageable));
    }

}
