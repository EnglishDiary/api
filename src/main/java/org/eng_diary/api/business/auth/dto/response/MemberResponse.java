package org.eng_diary.api.business.auth.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    @Schema(name = "loginId", example = "mytestId123")
    private Long loginId;

    @Schema(name = "nickname", example = "John Doe")
    private String nickname;

}
