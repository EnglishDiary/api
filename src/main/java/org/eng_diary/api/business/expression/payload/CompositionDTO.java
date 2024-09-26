package org.eng_diary.api.business.expression.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompositionDTO {
    private Long id;
    private String resultSentence;
    private String translation;
    private Long parentId;

}
