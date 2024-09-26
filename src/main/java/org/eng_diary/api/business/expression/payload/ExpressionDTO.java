package org.eng_diary.api.business.expression.payload;

import lombok.Getter;
import lombok.Setter;
import org.eng_diary.api.business.auth.payload.UserDTO;

import java.util.List;

@Getter
@Setter
public class ExpressionDTO {
    private Long id;
    private String summary;
    private String originalSentence;
    private String userSentence;
    private List<CompositionDTO> compositions;
    private UserDTO user;

}
