package org.eng_diary.api.domain.member.dto.response;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken
) {
}
