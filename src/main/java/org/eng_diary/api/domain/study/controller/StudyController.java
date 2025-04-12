package org.eng_diary.api.domain.study.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.study.service.AiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final AiService aiService;

    @PostMapping("/expression")
    public void analyzeExpression() {
        aiService.studyExpression(1L);
    }


}
