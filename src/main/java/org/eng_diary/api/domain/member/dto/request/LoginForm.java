package org.eng_diary.api.domain.member.dto.request;

import lombok.Getter;

@Getter
public class LoginForm {

    private String memberId;

    private String password;

}
