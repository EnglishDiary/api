package org.eng_diary.api.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String password;

    @Column(name = "member_name")
    private String name;

    private String email;

    @Column(name = "profile_url")
    private String profileImageUrl;

}
