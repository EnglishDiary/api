package org.eng_diary.api.domain.study.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.study.dto.request.ScriptUploadForm;
import org.eng_diary.api.domain.study.service.AiService;
import org.eng_diary.api.domain.study.service.StudyService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final AiService aiService;
    private final StudyService studyService;

    @PostMapping("/script/upload")
    public void uploadScript(@RequestBody ScriptUploadForm scriptUploadForm) {
        studyService.uploadScript(scriptUploadForm);
    }

    @PostMapping("/expression")
    public void analyzeExpression() {
        aiService.studyExpression(1L);
    }


}
