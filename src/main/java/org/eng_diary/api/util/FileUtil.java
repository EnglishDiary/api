package org.eng_diary.api.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {

    private static String s3Url;

    public static String getFileUrl(String uploadName) {
        return s3Url + uploadName;
    }

    @Value("${cloud.aws.s3.url}")
    public void setS3Url(String url) {
        s3Url = url;
    }
}
