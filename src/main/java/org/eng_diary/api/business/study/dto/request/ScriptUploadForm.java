package org.eng_diary.api.business.study.dto.request;

import java.util.List;

public record ScriptUploadForm(
        String script,
        String desc,
        List<String> sentences
) {
}
