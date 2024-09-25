package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Composition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "composition_id")
    private Long id;

    private String resultSentence;

    private String translation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expression_id")
    private Expression expression;

}
