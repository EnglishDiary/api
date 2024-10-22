package org.eng_diary.api.business.diary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.auth.model.User;
import org.eng_diary.api.business.diary.dto.DiaryDTO;
import org.eng_diary.api.business.diary.dto.DiaryDetailDTO;
import org.eng_diary.api.business.diary.dto.DiarySaveRequest;
import org.eng_diary.api.business.diary.dto.OfficialCategoryDTO;
import org.eng_diary.api.business.diary.repository.DiaryRepository;
import org.eng_diary.api.business.filemng.repository.FileManagerRepository;
import org.eng_diary.api.business.filemng.service.FileManagerService;
import org.eng_diary.api.domain.Diary;
import org.eng_diary.api.domain.FileMeta;
import org.eng_diary.api.domain.OfficialDiaryCategory;
import org.eng_diary.api.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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

    private final FileManagerService fileManagerService;
    private final DiaryRepository diaryRepository;
    private final FileManagerRepository fileManagerRepository;


    @Transactional
    public Map<String, Object> requestAICorrection(String userDiary) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String userMessage = "Please convert this diary into English, correct it, provide feedback in Korean, and translate the revised diary back to Korean:\n\n" + userDiary;

        String systemMessage = """
        You are an AI assistant that converts diaries into English, corrects them, provides feedback, and translates the revised version back to Korean. The input diary may be in Korean, English, or a mix of both. Please provide the corrected English diary, feedback in Korean, and the Korean translation of the revised diary in HTML format. Use the following JSON format for your response:
        {
          "revisedDiary": "The corrected and improved version of the diary in English, ensuring natural flow and context-appropriate language, in HTML format",
          "feedback": "다이어리에서 수정한 부분(문법적 오류교정 및 부자연스러운 표현 개선)에 대한 피드백들을 한국어로 작성한 HTML 포맷",
          "translation": "영어로 수정된 다이어리를 한국어로 번역한 버전, HTML 포맷"
        }
        Follow these steps:
        1. If the input is not in English, translate it to English.
        2. Correct and improve the English diary, focusing not only on grammar but also on enhancing the overall flow and natural expression of ideas.
        3. Provide feedback in Korean about all the corrections you made, including both grammatical fixes and improvements in natural expression and context.
        4. Translate the revised English diary back to Korean.
        Ensure that your entire response is valid JSON above. 
    """;

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
        Map<String, Object> usage = (Map<String, Object>) parsedResponse.get("usage");
        Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");

        String content = (String) message.get("content");
        String revisedDiary = "";
        String feedback = "";
        String translation = "";
        int totalTokens = Integer.parseInt(usage.get("total_tokens").toString());

        try {
            JsonNode jsonNode = objectMapper.readTree(content);
            revisedDiary = jsonNode.get("revisedDiary").asText();
            feedback = jsonNode.get("feedback").asText();
            translation = jsonNode.get("translation").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 원하는 포맷으로 재구성
        Map<String, Object> result = new HashMap<>();
        result.put("revisedDiary", revisedDiary);
        result.put("feedback", feedback);
        result.put("translation", translation);
        result.put("totalTokens", totalTokens);

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
    public void saveDiary(DiarySaveRequest diarySaveRequest, Long memberId, MultipartFile file) {

        User member = new User();
        member.setId(memberId);

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
                .translation(diarySaveRequest.getTranslation())
                .build();

        diaryRepository.saveDiary(diary);
        fileManagerService.saveFileMeta(file, "diary", diary.getId());
    }

    public Page<DiaryDTO> getDiaries(Long categoryId, Pageable pageable) {
        List<Diary> diaries = diaryRepository.findDiariesByCategory (categoryId, pageable);
        JPAQuery<Long> countQuery = diaryRepository.getDiaryCountQuery(categoryId);

        List<Long> diaryIds = diaries.stream().map(Diary::getId).toList();
        List<FileMeta> fileMetas = fileManagerRepository.findFileMetaList("diary", diaryIds);

        List<DiaryDTO> result = diaries.stream().map((diary) -> {
            String s3Url = fileMetas.stream()
                    .filter(file -> diary.getId().equals(file.getTableRowId()))
                    .findFirst()
                    .map(FileMeta::getUploadName)
                    .map(FileUtil::getFileUrl)
                    .orElse(null);

            DiaryDTO dto = new DiaryDTO();

            dto.setId(diary.getId());
            dto.setTitle(diary.getTitle());
            dto.setContent(diary.getContent());
            dto.setRegisterTime(diary.getRegisterTime());
            dto.setMemberName(diary.getMember().getName());
            dto.setMemberProfileUrl(diary.getMember().getImageUrl());
            dto.setThumbnailUrl(s3Url);

            // TODO 249030 아래 좋아요 댓글 하드코딩 구현 필요
            dto.setLikes(0);
            dto.setComments(0);
            return dto;
        }).collect(Collectors.toList());

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchCount);
    }

    public DiaryDetailDTO getDiaryDetail(Long diaryId) {
        Diary diary = diaryRepository.findDiary(diaryId);

        DiaryDetailDTO dto = new DiaryDetailDTO();

        dto.setId(diary.getId());
        dto.setTitle(diary.getTitle());
        dto.setContent(diary.getContent());
        dto.setMemberName(diary.getMember().getName());
        dto.setRegisterTime(diary.getRegisterTime());
        dto.setTranslation(diary.getTranslation());

        if (diary.isFeedbackPublic()) {
            dto.setAiFeedback(diary.getAiFeedback());
        }
        if (diary.isRevisionPublic()) {
            dto.setAiRevisedDiary(diary.getAiRevisedDiary());
        }

        // TODO 240908 하드코딩
        dto.setComments(0);
        dto.setLikes(0);
        dto.setMemberProfileUrl(diary.getMember().getImageUrl());
        dto.setThumbnailUrl("");

        return dto;
    }
}
