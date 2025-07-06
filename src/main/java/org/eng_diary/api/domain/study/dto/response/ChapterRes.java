package org.eng_diary.api.domain.study.dto.response;

import lombok.Builder;

@Builder
public record ChapterRes(
        Long id,
        String name,
        String desc,
        Long scriptId
) {
}
