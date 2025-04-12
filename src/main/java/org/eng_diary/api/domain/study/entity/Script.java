package org.eng_diary.api.domain.study.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Script {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "script_id")
    private Long id;

    @Column(name = "script_content")
    private String content;

}
