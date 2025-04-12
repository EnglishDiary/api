package org.eng_diary.api.domain.member.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String userId;

    private String password;

}
