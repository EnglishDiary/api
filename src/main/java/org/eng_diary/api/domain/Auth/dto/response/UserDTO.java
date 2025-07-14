package org.eng_diary.api.domain.Auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String role;
    private String profileImgUrl;

}
