package org.eng_diary.api.business.member.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String userId;

    private String password;

}
