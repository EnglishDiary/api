package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Fetch;

@Entity
@Getter
public class MemberWordPhonetic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_id")
    private Long id;

    private String count;

    private String soundUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_word_id")
    private MemberWord memberWord;

}
