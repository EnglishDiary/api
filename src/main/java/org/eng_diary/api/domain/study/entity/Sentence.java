package org.eng_diary.api.domain.study.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Sentence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sentence_id")
    private Long id;

    private String passage;

}
