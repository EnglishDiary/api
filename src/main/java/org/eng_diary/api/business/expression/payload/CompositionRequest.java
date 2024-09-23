package org.eng_diary.api.business.expression.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CompositionRequest {

    @NotBlank
    private String koreanSentence;

    private String userEnglishAttempt;

}
