package org.eng_diary.api.domain.auth.dto.request;

public record LoginForm(
        String loginId,
        String password
) { }
