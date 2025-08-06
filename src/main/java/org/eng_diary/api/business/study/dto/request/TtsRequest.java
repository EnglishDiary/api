package org.eng_diary.api.business.study.dto.request;

import lombok.Getter;

@Getter
public class TtsRequest {

    private String text;
    private String languageCode = "en-US"; // 기본값

}
