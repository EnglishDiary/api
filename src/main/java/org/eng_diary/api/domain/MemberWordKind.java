package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberWordKind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kind_id")
    private Long id;

    private String partOfSpeech;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_word_id")
    private MemberWord memberWord;

}
