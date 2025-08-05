package org.eng_diary.api.business.filemanage.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileMetaResponse {

    private Long fileId;
    private String uploadName;
    private String originalName;

}
