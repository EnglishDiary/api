package org.eng_diary.api.domain.member.dto.request;

public record LoginForm(
        String memberId,
        String password
) { }
