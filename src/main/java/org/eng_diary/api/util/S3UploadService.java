package org.eng_diary.api.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png", "image/gif");

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String saveFile(MultipartFile multipartFile, String uniqueFileName) throws IOException {
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            throw new IOException("The file size exceeds the maximum limit of 10MB.");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(multipartFile.getContentType())) {
            throw new IOException("File type not supported. Only JPEG, PNG, and GIF are allowed.");
        }

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, uniqueFileName, multipartFile.getInputStream(), metadata);
        return amazonS3.getUrl(bucket, uniqueFileName).toString();
    }

    public List<String> saveFiles(MultipartFile[] multipartFiles, List<String> uniqueFileNames) throws IOException {
        List<String> uploadedFileUrls = new ArrayList<>();

        for (int i=0; i< uniqueFileNames.size(); i++) {
            String fileName = uniqueFileNames.get(i);
            String fileUrl = saveFile(multipartFiles[i], fileName);

            uploadedFileUrls.add(fileUrl);
        }

        return uploadedFileUrls;
    }

}
