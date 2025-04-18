package org.eng_diary.api.domain.study.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.study.dto.request.ScriptUploadForm;
import org.eng_diary.api.domain.study.entity.Sentence;
import org.eng_diary.api.domain.study.repository.SentenceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AiService {

    @Value("${app.ai.openai.api.key}")
    private String apiKey;

    @Value("${app.ai.openai.api.url}")
    private String apiUrl;

    @Value("${app.ai.openai.api.maxTokens}")
    private Integer maxTokens;

    private final RestTemplate restTemplate;

    private final SentenceRepository sentenceRepository;

    public void studyExpression(Long sentenceId) {
        Sentence sentence = sentenceRepository.findById(sentenceId).orElse(null);
        HttpEntity<Map<String, Object>> requestEntity = createRequestEntity(sentence);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = response.getBody();

        Map<String, Object> parsedResponse = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            parsedResponse = objectMapper.readValue(responseBody, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        System.out.println("디버깅");
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(Sentence sentence) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String analysisTarget = "Janine: I'd say the main problem in this school district is, yeah, no money. Uh, the city says there isn't any, but they're doing a multimillion‐dollar renovation to the Eagles' stadium down the street from here. But we just make do. I mean, the staff here is incredible. They're all amazing teachers. I really look up to them all.";

        // 메시지 구성
        String userMessage = "여기서 would는 어떤 뜻으로 사용된 거지? 그리고 look up to가 무슨 뜻이야?";
        String systemMessage = """
            분석할 영어문장: %s
            컨텍스트:
            - 영어학습자 수준: B1
            - 미국드라마 애봇초등학교에 나오는 대사
        """.formatted(analysisTarget);

        // 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", List.of(
                Map.of("role", "system", "content", systemMessage),
                Map.of("role", "user", "content", userMessage)
        ));
        body.put("max_tokens", maxTokens);

        return new HttpEntity<>(body, headers);
    }

}
