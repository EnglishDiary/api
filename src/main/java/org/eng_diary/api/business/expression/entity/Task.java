package org.eng_diary.api.business.expression.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="job_id")
    private Job job;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "task")
    private List<Svr> svrs;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "task")
    private List<AttachFile> files;

}
