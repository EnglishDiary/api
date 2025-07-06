package org.eng_diary.api.domain.study.dto.request;

public record ChapterSaveForm(
        Long topicId,
        String name,
        String desc
) {
}
