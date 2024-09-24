package org.eng_diary.api.business.expression.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ExpressionSaveRequest {

    @Getter
    public static class Translation {
        @NotEmpty
        private String english;
        @NotEmpty
        private String korean;
    }

    private String originalSentence;
    private String summary;
    private String userSentence;

    @Valid
    private List<Translation> translations = new ArrayList<>();

}
