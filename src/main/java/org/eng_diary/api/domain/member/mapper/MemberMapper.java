package org.eng_diary.api.domain.member.mapper;

import org.eng_diary.api.domain.member.dto.response.MemberResponse;
import org.eng_diary.api.domain.member.dto.request.SignupForm;
import org.eng_diary.api.entity.Member;

public class MemberMapper {
    public static Member createMember(SignupForm signupForm) {
        return Member.builder()
                .nickname(signupForm.getNickname())
                .build();
    }

    public static MemberResponse createMemberResponse(Member member) {
        return MemberResponse.builder()
                .userId(member.getId())
                .nickname(member.getNickname())
                .build();
    }

}
