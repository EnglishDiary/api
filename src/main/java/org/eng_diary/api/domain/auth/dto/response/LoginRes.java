package org.eng_diary.api.domain.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken
) {
}
