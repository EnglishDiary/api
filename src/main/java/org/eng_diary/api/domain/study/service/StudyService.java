package org.eng_diary.api.domain.study.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.common.context.UserContextHolder;
import org.eng_diary.api.domain.study.dto.request.ScriptUploadForm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {
    @Transactional
    public void uploadScript(ScriptUploadForm scriptUploadForm) {
        List<String> sentences = scriptUploadForm.script();

        String userId = UserContextHolder.getUserId();
        System.out.println("userId -> " + userId);

        System.out.println(sentences);
    }

}
