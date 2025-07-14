package org.eng_diary.api.business.study.dto.response;

import lombok.Builder;

@Builder
public record ConversationRes(
        Long id,
        String userQuestion,
        String aiAnswer
) {
}
