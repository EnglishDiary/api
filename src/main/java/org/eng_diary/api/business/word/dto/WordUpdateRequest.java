package org.eng_diary.api.business.word.dto;

import lombok.Getter;

@Getter
public class WordUpdateRequest {

    private Long wordId;
    private Long categoryId;
    private String word;
    private String jsonStr;

}
