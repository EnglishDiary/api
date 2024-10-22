package org.eng_diary.api.business.diary.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DiaryDetailDTO {

    private Long id;

    private String title;

    private String content;

    private String aiRevisedDiary;

    private String aiFeedback;

    private LocalDateTime registerTime;

    private String memberName;

    private String memberProfileUrl;

    private String thumbnailUrl;

    private Integer likes;

    private Integer comments;

    private String translation;



}
