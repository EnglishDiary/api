package org.eng_diary.api.domain.study.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.study.dto.request.AiAskingForm;
import org.eng_diary.api.domain.study.dto.response.AiAnswerRes;
import org.eng_diary.api.domain.study.entity.*;
import org.eng_diary.api.domain.study.mapper.StudyMapper;
import org.eng_diary.api.domain.study.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
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
    private final ScriptRepository scriptRepository;
    private final ChapterRepository chapterRepository;
    private final TopicRepository topicRepository;
    private final ConversationRepository conversationRepository;

    @Transactional
    public AiAnswerRes askAiSentence(AiAskingForm aiAskingForm) {
        HttpEntity<Map<String, Object>> requestEntity = createRequestEntity(aiAskingForm);

        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> parsedResponse;

        try {
            parsedResponse = objectMapper.readValue(responseBody, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        List choices = (ArrayList) parsedResponse.get("choices");
        Map firstChoice = (HashMap) choices.get(0);
        Map message = (HashMap) firstChoice.get("message");
        String aiAnswer = message.get("content").toString();

        saveConversation(aiAskingForm, aiAnswer);

        return AiAnswerRes.builder()
                .answer(aiAnswer)
                .build();
    }

    private void saveConversation(AiAskingForm aiAskingForm, String aiAnswer) {
        Sentence sentence = sentenceRepository.findByIdOrThrow(aiAskingForm.sentenceId());

        Conversation conversation = StudyMapper.createConversation(aiAskingForm, aiAnswer, sentence);
        conversationRepository.save(conversation);
    }

    private HttpEntity<Map<String, Object>> createRequestEntity(AiAskingForm aiAskingForm) {

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        // 메시지 구성
        String userMessage = aiAskingForm.question();
        String systemMessage = getSentenceContext(aiAskingForm);;

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

    private String getSentenceContext(AiAskingForm aiAskingForm) {
        Sentence sentence = sentenceRepository.findByIdOrThrow(aiAskingForm.sentenceId());
        Script script = scriptRepository.findById(aiAskingForm.scriptId())
                .orElseThrow(() -> new RuntimeException("not existed script"));
        Chapter chapter = chapterRepository.findById(aiAskingForm.chapterId())
                .orElseThrow(() -> new RuntimeException("not existed chapter"));
        Topic topic = topicRepository.findById(aiAskingForm.topicId())
                .orElseThrow(() -> new RuntimeException("not existed topic"));

        String analysisTarget = sentence.getPassage();
        String topicDesc = topic.getDesc();
        String chapterDesc = chapter.getDesc();
        String scriptDesc = script.getDesc();
        List<String> linkedSentences = aiAskingForm.linkedSentences();

        StringBuilder linkedSentencesResult = new StringBuilder();
        for (String linkedSentence : linkedSentences) {
            linkedSentencesResult.append(linkedSentence).append("\n");
        }

        return """            
분석할 영어문장: %s

<사용자 제공 정보>
- 사용자의 영어수준: B1
- 대주제: %s
- 소주제: %s
- 현재 영어문장의 맥락: %s
- 이전 혹은 다음 문장을 함께 포함한 결과: %s

응답결과는 마크다운 형식으로 작성하고, 한국어로 설명해주세요.
""".formatted(analysisTarget, topicDesc, chapterDesc, scriptDesc, linkedSentencesResult.toString());
    }

}
