package org.eng_diary.api.business.word.dto;

import lombok.Getter;

@Getter
public class WordUpdateRequest {

    private Long wordId;
    private String word;
    private String jsonStr;

}
