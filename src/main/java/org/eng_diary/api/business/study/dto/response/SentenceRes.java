package org.eng_diary.api.business.study.dto.response;

import lombok.Builder;

@Builder
public record SentenceRes(
        Long id,
        String passage

) {
}
