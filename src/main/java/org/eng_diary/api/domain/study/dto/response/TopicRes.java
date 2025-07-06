package org.eng_diary.api.domain.study.dto.response;

import lombok.Builder;

@Builder
public record TopicRes (
        Long id,
        String name,
        String desc
)
{ }
