package org.eng_diary.api.common.context;

import lombok.Builder;

@Builder
public record UserContext(
        Long memberId,
        String loginId
) {
}
