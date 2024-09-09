package org.eng_diary.api.business.auth.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


/**
 * Created by rajeevkumarsingh on 02/08/17.
 */

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String imageUrl;

}
