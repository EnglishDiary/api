package org.eng_diary.api.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.TextToSpeechSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class GoogleCloudConfig {

    @Value("${google.cloud.credentials.location:}")
    private String credentialsLocation;

    @Bean
    public TextToSpeechClient textToSpeechClient() throws IOException {
        if (!credentialsLocation.isEmpty()) {
            InputStream credentialsStream = getClass().getClassLoader()
                    .getResourceAsStream(credentialsLocation.replace("classpath:", ""));
            GoogleCredentials credentials = GoogleCredentials.fromStream(credentialsStream);

            TextToSpeechSettings settings = TextToSpeechSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                    .build();

            return TextToSpeechClient.create(settings);
        }

        // 환경변수로 설정된 경우 기본 설정 사용
        return TextToSpeechClient.create();
    }
}
