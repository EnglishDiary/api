package org.eng_diary.api.domain.Auth.dto.response;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken
) {
}
