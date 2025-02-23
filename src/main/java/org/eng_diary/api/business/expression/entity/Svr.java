package org.eng_diary.api.business.expression.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "svr")
public class Svr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "svr_id")
    private Long id;

    private String ip;

    private String hostNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

}
