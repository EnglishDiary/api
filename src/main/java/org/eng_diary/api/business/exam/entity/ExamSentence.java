package org.eng_diary.api.business.exam.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.eng_diary.api.common.entity.BaseEntity;

@Entity
@Getter

public class ExamSentence extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id")
    private Long id;

    private String sourceSentence;

    private String translation;

    private String level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private ExamSet examSet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private ExamTopic examTopic;
}
