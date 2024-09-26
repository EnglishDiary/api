package org.eng_diary.api.business.expression.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.auth.payload.UserDTO;
import org.eng_diary.api.business.expression.payload.CompositionDTO;
import org.eng_diary.api.business.expression.payload.CompositionRequest;
import org.eng_diary.api.business.expression.payload.ExpressionDTO;
import org.eng_diary.api.business.expression.payload.ExpressionSaveRequest;
import org.eng_diary.api.business.expression.repository.ExpressionRepository;
import org.eng_diary.api.domain.Composition;
import org.eng_diary.api.domain.Expression;
import org.eng_diary.api.domain.Member;
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
public class ExpressionService {

    @Value("${app.ai.openai.api.key}")
    private String apiKey;

    @Value("${app.ai.openai.api.url}")
    private String apiUrl;

    @Value("${app.ai.openai.api.maxTokens}")
    private Integer maxTokens;

    private final RestTemplate restTemplate;

    private final ExpressionRepository expressionRepository;

    public Map<String, Object> requestAICorrection(CompositionRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("Content-Type", "application/json");

        String systemMessage = """
            당신은 한국어를 영어로 번역하는 전문가입니다. 주어진 한국어 문장을 영어로 번역하여 3개의 영어 문장을 만들어야 합니다. 사용자가 영어 문장을 제공한 경우, 이를 첫 번째 번역으로 사용하고 개선하세요. 각 영어 문장에 대해 한국어로 다시 번역해야 합니다. 또한, 당신의 영작 과정과 결과에 대한 간단한 요약을 제공해야 합니다. 응답은 반드시 지정된 JSON 형식으로 제공해야 합니다. 번역시 다음 지침을 따르세요:
            1. 원래 의미를 정확히 전달하되, 자연스러운 영어 표현을 사용하세요.
            2. 문맥에 맞는 다양한 어휘와 문법 구조를 사용하세요.
            3. 사용자가 영어 문장을 제공한 경우, 이를 개선할 때는 문법 오류를 수정하고 더 자연스러운 표현으로 바꾸세요.
            4. 한국어로 다시 번역할 때는 원래 한국어 문장과 완전히 같지 않아도 됩니다. 영어 문장의 뉘앙스를 잘 살려주세요.
            5. 요약에서는 번역 과정에서 고려한 주요 사항, 선택한 표현의 이유, 그리고 번역의 전반적인 특징을 간략히 설명하세요.
        """;

        String userMessageTemplate = """
            다음 한국어 문장을 영어로 번역해주세요. 총 3개의 영어 문장과 각각의 한국어 번역을 JSON 형식으로 제공해주세요.
            한국어 문장: %s
            사용자 영어 문장: %s
        
            JSON 응답 형식:
            {
              "originalSentence": "원래 한국어 문장",
              "translations": [
                {
                  "english": "영어 번역 1",
                  "korean": "한국어 역번역 1"
                },
                {
                  "english": "영어 번역 2",
                  "korean": "한국어 역번역 2"
                },
                {
                  "english": "영어 번역 3",
                  "korean": "한국어 역번역 3"
                }
              ],
              "translationSummary": "영작 과정과 결과에 대한 간단한 요약"
            }
        """;

        String koreanSentence = request.getKoreanSentence();
        String userEnglishAttempt = request.getUserEnglishAttempt();
        String userMessage = String.format(userMessageTemplate, koreanSentence, userEnglishAttempt);

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

        HttpEntity<Map<String, Object>> httpRequest = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, httpRequest, String.class);

        // 응답 처리
        String responseBody = response.getBody();

        // OpenAI API의 응답을 JSON으로 파싱하여 필요한 필드를 추출
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> parsedResponse = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> choices = (List<Map<String, Object>>) parsedResponse.get("choices");
            Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
            String content = (String) message.get("content");

            // AI의 응답(JSON 문자열)을 Map으로 파싱
            Map<String, Object> aiResponseMap = objectMapper.readValue(content, Map.class);

            result.put("originalSentence", aiResponseMap.get("originalSentence"));
            result.put("translations", aiResponseMap.get("translations"));
            result.put("summary", aiResponseMap.get("translationSummary"));

            // 사용량 정보 추가
            Map<String, Object> usage = (Map<String, Object>) parsedResponse.get("usage");
            result.put("usage", usage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            result.put("error", "Failed to parse AI response");
        }

        return result;
    }

    @Transactional
    public void saveExpression(ExpressionSaveRequest request, Long memberId) {
        Member member = new Member();
        member.setId(memberId);

        Expression expression = new Expression();
        expression.setSummary(request.getSummary());
        expression.setOriginalSentence(request.getOriginalSentence());
        expression.setUserSentence(request.getUserSentence());
        expression.setMember(member);
        expressionRepository.saveExpression(expression);

        for (ExpressionSaveRequest.Translation translation : request.getTranslations()) {
            Composition composition = new Composition();
            composition.setResultSentence(translation.getEnglish());
            composition.setTranslation(translation.getKorean());
            composition.setExpression(expression);
            expressionRepository.saveComposition(composition);
        }
    }

    public List<ExpressionDTO> getExpressionList() {
        List<Expression> expressions = expressionRepository.getExpressionList();

        List<ExpressionDTO> collect = expressions.stream().map((exp) -> {
            ExpressionDTO expressionDTO = new ExpressionDTO();
            expressionDTO.setId(exp.getId());
            expressionDTO.setSummary(exp.getSummary());
            expressionDTO.setOriginalSentence(exp.getOriginalSentence());
            expressionDTO.setUserSentence(exp.getUserSentence());

            List<CompositionDTO> compositionDTOList = exp.getCompositions().stream().map((com) -> {
                CompositionDTO compositionDTO = new CompositionDTO();
                compositionDTO.setId(com.getId());
                compositionDTO.setParentId(exp.getId());
                compositionDTO.setResultSentence(com.getResultSentence());
                compositionDTO.setTranslation(com.getTranslation());
                return compositionDTO;
            }).collect(Collectors.toList());
            expressionDTO.setCompositions(compositionDTOList);

            Member member = exp.getMember();
            UserDTO userInfo = new UserDTO();
            userInfo.setName(member.getName());
            userInfo.setProfileImgUrl(member.getProfileImageUrl());
            expressionDTO.setUser(userInfo);

            return expressionDTO;
        }).collect(Collectors.toList());
        return collect;
    }
}
