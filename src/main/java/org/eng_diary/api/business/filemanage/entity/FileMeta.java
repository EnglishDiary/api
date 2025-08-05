package org.eng_diary.api.business.filemanage.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "file_meta")
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

    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relation_id")
    private EntityFileRelation entityFileRelation;

}
