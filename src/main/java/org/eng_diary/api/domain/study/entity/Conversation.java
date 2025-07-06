package org.eng_diary.api.domain.study.entity;

import jakarta.persistence.*;
import lombok.*;
import org.eng_diary.api.entity.BaseEntity;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "conversation")
public class Conversation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversation_id")
    private Long id;

    @Column(name = "user_question")
    private String userQuestion;

    @Column(name = "ai_answer")
    private String aiAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sentence_id")
    private Sentence sentence;

}
