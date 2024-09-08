package org.eng_diary.api.business.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)[a-z\\d]{5,20}$",
            message = "아이디는 5자 이상 20자 이하의 영문 소문자와 숫자의 조합이어야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Size(min = 1, max = 10, message = "닉네임은 1자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "^[가-힣\\u318D\\u119E\\u11A2\\u2022\\u2025a-zA-Z0-9]{1,20}$",
            message = "닉네임은 한글, 영문, 숫자로 이루어진 1자 이상 10자 이하여야 합니다.")
    private String nickname;

    private String profileImageUrl;

}
