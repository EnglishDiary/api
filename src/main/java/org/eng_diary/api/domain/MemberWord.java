package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class MemberWord extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_word_id")
    private Long id;

    private String word;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_word_category_id")
    private MemberWordCategory memberWordCategory;

    @OneToMany(mappedBy = "memberWord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberWordKind> kinds;

}
