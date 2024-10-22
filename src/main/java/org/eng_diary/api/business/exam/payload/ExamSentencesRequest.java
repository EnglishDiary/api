package org.eng_diary.api.business.exam.payload;

import lombok.Getter;
import lombok.Setter;
import org.eng_diary.api.constant.Level;

@Getter
@Setter
public class ExamSentencesRequest {

    private Level level;
    private String examType;
    private Long topicId;
    private Integer sentenceCount;
    private String sentenceSet;

}
