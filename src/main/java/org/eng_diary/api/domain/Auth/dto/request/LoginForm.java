package org.eng_diary.api.domain.Auth.dto.request;

public record LoginForm(
        String loginId,
        String password
) { }
