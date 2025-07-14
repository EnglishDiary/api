package org.eng_diary.api.business.study.dto.request;

import lombok.Getter;

import java.util.List;

public record AiAskingForm(
        String question,
        Long sentenceId,
        Long scriptId,
        Long chapterId,
        Long topicId,
        List<String> linkedSentences
) {
}
