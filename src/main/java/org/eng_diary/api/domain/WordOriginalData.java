package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "word_original_data")
public class WordOriginalData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "word_original_data_id")
    private Long id;

    @Column(name = "word_title")
    private String wordTitle;

    // TODO 240816 추후 칼럼 데이터타입을 json 타입으로 변경할 지 고민해보기
    private String jsonString;

}
