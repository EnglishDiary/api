package org.eng_diary.api.domain.study.service;

import lombok.RequiredArgsConstructor;
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

        System.out.println(response);
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(Sentence sentence) {
        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String hardCodedSentence = "Hiccup (v.o.): This is Berk. It's twelve days north of Hopeless, and a few degrees south of Freezing to Death.";

        // 메시지 구성
        String userMessage = "아래 영어문장을 전체적으로 분석해주세요(주목할 문법, 어려운 단어 혹은 숙어, 주요한 표현 위주로). 설정된 시스템메세지를 반드시 참고하여 답변하세요.\n\n" + sentence.getPassage();
        String systemMessage = """
            You are an AI assistant that helps Korean study English. Analyze the given English sentence or passage.
            Provide your response in the following JSON format:
            
            {
              "result": "여기에 마크다운 형식으로 영어 문장 분석 내용을 작성해주세요. 문장에 사용된 주요 문법, 어려운 단어, 숙어, 주요 표현 등을 설명해주세요."
            }
            
            결과는 마크다운 형식으로 작성하고, 한국어로 설명해주세요. 전체 응답이 유효한 JSON 형식인지 확인하세요.
        """;

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
