package org.eng_diary.api.business.expression.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.eng_diary.api.common.entity.BaseEntity;
import org.eng_diary.api.business.auth.entity.Member;

import java.util.List;

@Entity
@Getter
@Setter
public class Expression extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expression_id")
    private Long id;

    private String summary;

    private String originalSentence;

    private String userSentence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "expression")
    private List<Composition> compositions;

}
