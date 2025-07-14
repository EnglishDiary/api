package org.eng_diary.api.business.study.dto.response;

import lombok.Builder;

@Builder
public record ChapterRes(
        Long id,
        String name,
        String desc,
        Long scriptId,
        Integer bookmarkIndex
) {
}
