package org.eng_diary.api.domain.auth.mapper;

import org.eng_diary.api.domain.auth.dto.response.MemberResponse;
import org.eng_diary.api.domain.auth.dto.request.SignupForm;
import org.eng_diary.api.entity.Member;

public class MemberMapper {
    public static Member createMember(SignupForm signupForm, String encodedPassword) {
        return Member.builder()
                .loginId(signupForm.getMemberId())
                .password(encodedPassword)
                .nickname(signupForm.getNickname())
                .build();
    }

    public static MemberResponse createMemberResponse(Member member) {
        return MemberResponse.builder()
                .loginId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

}
