package org.eng_diary.api.business.auth.dto.request;

public record LoginForm(
        String loginId,
        String password
) { }
