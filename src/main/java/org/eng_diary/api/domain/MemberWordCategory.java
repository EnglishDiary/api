package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MemberWordCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_word_category_id")
    private Long id;

    private String categoryName;

    private Long categoryOwnerId;
}
