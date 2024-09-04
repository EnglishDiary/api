package org.eng_diary.api.business.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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
public class DiaryService {

    @Value("${app.ai.openai.api.key}")
    private String apiKey;

    @Value("${app.ai.openai.api.url}")
    private String apiUrl;

    @Value("${app.ai.openai.api.maxTokens}")
    private Integer maxTokens;

    private final RestTemplate restTemplate;

    @Transactional
    public Map<String, String> requestAICorrection() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String originalDiary = """
                Title: 11th, January (Mon) Diary
                                
                Wanna improve my english skills but there is not much time for me to practice using English.
                Right. That's an excuse. Totally excuse. You know what? I have a plenty of time to do anything even though I'm in progress of hard training where it starts at 9:30 am and ends at 6:30pm.
                So, don't blame other things, but just do your thing. That's what I know when it comes to solving the problem we face.
                """;

        String userMessage = "Please correct this English diary and provide feedback in Korean:\n\n" + originalDiary;

        // ChatGPT에게 보낼 메시지 생성
        String systemMessage = """
                You are an AI assistant that corrects English diaries. Please provide the corrected diary in English and feedback in Korean. Use the following JSON format for your response:
                {
                  "revisedDiary": "The corrected version of the diary in English",
                  "feedback": "첨삭한 부분들에 대한 피드백을 한국어로 작성"
                }
                Ensure that your entire response is valid JSON.""";

        // 요청 바디 생성
        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-4o-mini");
        body.put("messages", new Object[]{
                new HashMap<String, String>() {{
                    put("role", "system");
                    put("content", systemMessage);
                }},
                new HashMap<String, String>() {{
                    put("role", "user");
                    put("content", userMessage);
                }}
        });
        body.put("max_tokens", maxTokens);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, String.class);

        // 응답 처리
        String responseBody = response.getBody();

        // OpenAI API의 응답을 JSON으로 파싱하여 필요한 필드를 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parsedResponse = null;
        try {
            parsedResponse = objectMapper.readValue(responseBody, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) parsedResponse.get("choices");

        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        String content = (String) message.get("content");
        String revisedDiary = "";
        String feedback = "";
        try {
            JsonNode jsonNode = objectMapper.readTree(content);
            revisedDiary = jsonNode.get("revisedDiary").asText();
            feedback = jsonNode.get("feedback").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 원하는 포맷으로 재구성
        Map<String, String> result = new HashMap<>();
        result.put("revisedDiary", revisedDiary);
        result.put("feedback", feedback);

        return result;
    }

}
