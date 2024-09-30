package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String uploadName;

    private String originalName;

    @Column(name = "extension")
    private String ext;

    private Long size;

    private String referencedTable;

    private Long tableRowId;

}
