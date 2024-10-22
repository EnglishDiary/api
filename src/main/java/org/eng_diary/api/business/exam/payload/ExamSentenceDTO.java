package org.eng_diary.api.business.exam.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamSentenceDTO {

    private Long id;
    private String sourceSentence;
    private String translation;
    private String level;

}
