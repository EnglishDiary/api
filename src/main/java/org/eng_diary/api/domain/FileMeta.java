package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class FileMeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    private String uploadName;

    private String originalName;

    @Column(name = "extension")
    private String ext;

    private String referencedTable;

    private String tableRowId;

}
