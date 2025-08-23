package org.eng_diary.api.business.filemanage.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.filemanage.dao.EntityFileRelationJpaRepository;
import org.eng_diary.api.business.filemanage.dao.FileMetaJpaRepository;
import org.eng_diary.api.business.filemanage.dto.response.FileMetaResponse;
import org.eng_diary.api.business.filemanage.entity.EntityFileRelation;
import org.eng_diary.api.business.filemanage.entity.FileMeta;
import org.eng_diary.api.util.S3UploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileManagerService {

    private final S3UploadService s3UploadService;
    private final FileMetaJpaRepository fileMetaJpaRepository;
    private final EntityFileRelationJpaRepository entityFileRelationJpaRepository;

    public void saveFile(MultipartFile file, EntityFileRelation relation) {
        if (file == null) {
            return;
        }

        String originalFileName = file.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        }

        String originalFileNameWithoutExt = originalFileName.replace("." + extension, "");
        String uniqueFileName = "dbgd-" +UUID.randomUUID();
        long fileSize =  file.getSize();

        FileMeta fileMeta = FileMeta.builder()
                .uploadName(uniqueFileName)
                .originalName(originalFileNameWithoutExt)
                .ext(extension)
                .size(fileSize)
                .entityFileRelation(relation)
                .build();

        fileMetaJpaRepository.save(fileMeta);
        s3UploadService.saveFile(file, uniqueFileName);
    }

    public List<FileMetaResponse> getFileMetaList(EntityFileRelation entityFileRelation) {

        if (entityFileRelation == null) {
            return Collections.emptyList();
        }

        List<FileMeta> fileMetaList = fileMetaJpaRepository.findByEntityFileRelation(entityFileRelation);

        return fileMetaList.stream().map((fileMeta) -> {
            FileMetaResponse fileMetaResponse = new FileMetaResponse();
            fileMetaResponse.setFileId(fileMeta.getId());
            fileMetaResponse.setOriginalName(fileMeta.getOriginalName());
            fileMetaResponse.setUploadName(fileMeta.getUploadName());

            return fileMetaResponse;
        }).toList();
    }


}
