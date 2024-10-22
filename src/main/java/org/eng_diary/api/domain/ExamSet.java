package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ExamSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name ="topic_id")
    private ExamTopic topic;

}
