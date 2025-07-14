package org.eng_diary.api.business.diary.entity;

import jakarta.persistence.*;
import lombok.*;
import org.eng_diary.api.common.entity.BaseEntity;
import org.eng_diary.api.business.auth.entity.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Column(name = "diary_title")
    private String title;

    @Column(name = "diary_content")
    private String content;

    private String aiRevisedDiary;

    private String aiFeedback;

    private boolean isDiaryPublic;

    private boolean isRevisionPublic;

    private boolean isFeedbackPublic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "official_diary_category_id")
    private OfficialDiaryCategory officialDiaryCategory;
}
