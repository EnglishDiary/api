package org.eng_diary.api.business.expression.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
class svrDTO {
    private Long serverId;
    private String ip;
    private String hostNm;
}

@Getter
@Setter
class AttachFileDTO {
    private Long fileId;
    private String fileName;
}

@Getter
@Setter
public class TaskDTO {

    private Long taskId;
    private String taskName;
    private List<AttachFileDTO> attachFileDTOList;
    private List<svrDTO> svrDTOList;

}
