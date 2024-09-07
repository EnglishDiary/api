package org.eng_diary.api.business.diary.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiarySaveRequest {

    private String title;

    private String content;

    private String aiRevisedDiary;

    private String aiFeedback;

    @JsonProperty("isPublic")
    private boolean isPublic;

    @JsonProperty("isRevisionPublic")
    private boolean isRevisionPublic;

    @JsonProperty("isFeedbackPublic")
    private boolean isFeedbackPublic;

    private Long officialCategoryId;

}
