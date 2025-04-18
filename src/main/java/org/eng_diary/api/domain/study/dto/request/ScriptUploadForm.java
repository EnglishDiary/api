package org.eng_diary.api.domain.study.dto.request;

import java.util.List;

public record ScriptUploadForm(
        List<String> script
) {
}
