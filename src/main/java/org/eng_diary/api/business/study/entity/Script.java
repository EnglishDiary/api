package org.eng_diary.api.business.study.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "script")
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "script_id")
    private Long id;

    @Column(name = "script_content")
    private String content;

    @Column(name = "script_desc")
    private String desc;

    @OneToMany(mappedBy = "script")
    private List<Sentence> sentences;

}
