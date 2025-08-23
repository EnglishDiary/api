package org.eng_diary.api.business.filemanage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "entity_file_relation")
@Getter
public class EntityFileRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relation_id")
    private Long id;

    @Column(name = "entity_name")
    @Setter
    private String entityName;

}
