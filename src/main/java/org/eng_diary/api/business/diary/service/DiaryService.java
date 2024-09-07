package org.eng_diary.api.business.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.diary.dto.DiaryDTO;
import org.eng_diary.api.business.diary.dto.DiarySaveRequest;
import org.eng_diary.api.business.diary.dto.OfficialCategoryDTO;
import org.eng_diary.api.business.diary.repository.DiaryRepository;
import org.eng_diary.api.domain.Diary;
import org.eng_diary.api.domain.Member;
import org.eng_diary.api.domain.OfficialDiaryCategory;
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
import java.util.stream.Collectors;

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

    private final DiaryRepository diaryRepository;

    @Transactional
    public Map<String, String> requestAICorrection(String userDiary) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String userMessage = "Please correct this English diary and provide feedback in Korean:\n\n" + userDiary;

        // ChatGPT에게 보낼 메시지 생성
        String systemMessage = """
            You are an AI assistant that corrects English diaries. Please provide the corrected diary and feedback in HTML format. Use the following JSON format for your response:
            {
              "revisedDiary": "<p>The corrected version of the diary in English in HTML format</p>",
              "feedback": "<p>첨삭한 부분들에 대한 피드백을 한국어로 작성한 HTML 포맷</p>"
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

    public List<OfficialCategoryDTO> getOfficialCategory() {
        List<OfficialDiaryCategory> officialCategory = diaryRepository.findOfficialCategory();

        return officialCategory.stream().map((category) -> {
            OfficialCategoryDTO dto = new OfficialCategoryDTO();
            dto.setId(category.getId());
            dto.setName(category.getName());
            dto.setSeq(category.getSeq());
            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional
    public void saveDiary(DiarySaveRequest diarySaveRequest) {

        // TODO 240906 멤버 하드코딩 제거
        Member member = new Member();
        member.setId(1L);

        // TODO 240906 프론트에서 카테고리 id 받은 거 db에 select 한 번 날려보긴 해야 함
        OfficialDiaryCategory officialDiaryCategory = new OfficialDiaryCategory();
        officialDiaryCategory.setId(diarySaveRequest.getOfficialCategoryId());

        if (!diarySaveRequest.isPublic()) {
            diarySaveRequest.setRevisionPublic(false);
            diarySaveRequest.setFeedbackPublic(false);
        }

        Diary diary = Diary.builder()
                .title(diarySaveRequest.getTitle())
                .content(diarySaveRequest.getContent())
                .aiRevisedDiary(diarySaveRequest.getAiRevisedDiary())
                .aiFeedback(diarySaveRequest.getAiFeedback())
                .isDiaryPublic(diarySaveRequest.isPublic())
                .isRevisionPublic(diarySaveRequest.isRevisionPublic())
                .isFeedbackPublic(diarySaveRequest.isFeedbackPublic())
                .member(member)
                .officialDiaryCategory(officialDiaryCategory)
                .build();

        diaryRepository.saveDiary(diary);
    }

    public List<DiaryDTO> getDiaries(Long categoryId) {
        List<Diary> diaries = diaryRepository.findDiariesByCategory(categoryId);

        return diaries.stream().map((diary) -> {
            DiaryDTO dto = new DiaryDTO();
            dto.setTitle(diary.getTitle());
            dto.setContent(diary.getContent());
            dto.setRegisterTime(diary.getRegisterTime());
            dto.setMemberName(diary.getMember().getName());
            dto.setMemberProfileUrl("");    // TODO 240906 프로필 url 구현 필요
            return dto;
        }).collect(Collectors.toList());
    }
}
