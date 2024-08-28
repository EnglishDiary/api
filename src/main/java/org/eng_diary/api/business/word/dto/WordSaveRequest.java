package org.eng_diary.api.business.word.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WordSaveRequest {

    private String jsonData;
    private Long categoryId;

}
