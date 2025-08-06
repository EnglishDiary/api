package org.eng_diary.api.business.study.service;

import com.google.cloud.texttospeech.v1.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TextToSpeechService {

    private final TextToSpeechClient textToSpeechClient;

    public byte[] synthesizeSpeech(String text, String languageCode) {
        try {
            // 입력 텍스트 설정
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            // 음성 설정 (영어학습용이므로 영어 음성으로 설정)
            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode(languageCode) // "en-US", "en-GB" 등
                    .setSsmlGender(SsmlVoiceGender.FEMALE) // 또는 MALE
                    .setName("en-US-Standard-C") // 특정 음성 선택 (선택사항)
                    .build();

            // 오디오 설정
            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .setSpeakingRate(1.0) // 학습용으로 약간 느리게
                    .build();

            // TTS 요청
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(
                    input, voice, audioConfig);

            return response.getAudioContent().toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("TTS 변환 중 오류 발생", e);
        }
    }
}
