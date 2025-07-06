package org.eng_diary.api.domain.study.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ScriptRes(
        String content,
        List<SentenceRes> sentences
) {
}
