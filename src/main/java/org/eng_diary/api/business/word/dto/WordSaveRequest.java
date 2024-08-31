package org.eng_diary.api.business.word.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WordSaveRequest {

    private String jsonData;
    private Long categoryId;
    private LocalDateTime registerTime;

}
