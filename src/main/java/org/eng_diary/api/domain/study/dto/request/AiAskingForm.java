package org.eng_diary.api.domain.study.dto.request;

import lombok.Getter;

public record AiAskingForm(
        String question,
        Long sentenceId,
        Long scriptId,
        Long chapterId,
        Long topicId
) {
}
