package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MemberWordExample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "example_id")
    private Long id;

    private String example;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meaning_id")
    private MemberWordMeaning meaning;

    private String provider;

}
