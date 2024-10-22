package org.eng_diary.api.domain;

import jakarta.persistence.*;

@Entity
public class ExamTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private Long id;

    @Column(name = "topic_name")
    private String name;
}
