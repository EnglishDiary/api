package org.eng_diary.api.business.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginRes(
        String accessToken
) {
}
