package org.eng_diary.api.business.filemanage.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileMetaSaveForm {

    private String originalName;
    private String uploadName;
    private String extension;
    private Long size;
    private Long relationId;

}
