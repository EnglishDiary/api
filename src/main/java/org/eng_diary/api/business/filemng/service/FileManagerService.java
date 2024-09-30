package org.eng_diary.api.business.filemng.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.filemng.repository.FileManagerRepository;
import org.eng_diary.api.domain.FileMeta;
import org.eng_diary.api.util.S3UploadService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileManagerService {
    private final FileManagerRepository fileManagerRepository;
    private final S3UploadService s3UploadService;

    @Transactional
    public void saveFileMeta(MultipartFile file, String tableName, Long tableRowId) {
        if (file == null) {
            return;
        }

        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        }

        String uniqueFileName = UUID.randomUUID() + "." + extension;
        long size = file.getSize();

        FileMeta fileMeta = FileMeta.builder()
                .originalName(originalFilename)
                .uploadName(uniqueFileName)
                .size(size)
                .ext(extension)
                .referencedTable(tableName)
                .tableRowId(tableRowId)
                .build();
        fileManagerRepository.saveFileMeta(fileMeta);
        s3UploadService.saveFile(file, uniqueFileName);
    }


}
