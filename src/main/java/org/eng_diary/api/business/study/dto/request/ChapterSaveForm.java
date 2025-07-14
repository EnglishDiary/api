package org.eng_diary.api.business.study.dto.request;

public record ChapterSaveForm(
        Long topicId,
        String name,
        String desc
) {
}
