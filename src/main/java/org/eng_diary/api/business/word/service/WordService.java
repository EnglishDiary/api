package org.eng_diary.api.business.word.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.repository.WordRepository;
import org.eng_diary.api.domain.Member;
import org.eng_diary.api.domain.WordOriginalData;
import org.eng_diary.api.exception.customError.BadRequestError;
import org.eng_diary.api.exception.customError.OpenApiServerError;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WordService {
    private final WordRepository wordRepository;
    private final RestTemplate restTemplate;
    private final String[] countries = {"uk", "us"};

    // TODO 240815 RestTemplateBuilder bean 생성
    public WordService(WordRepository wordRepository, RestTemplateBuilder restTemplateBuilder) {
        this.wordRepository = wordRepository;
        this.restTemplate = restTemplateBuilder.build();
    }

    public MemberResponse testService() {
        Member member = wordRepository.findMember();
        Member member2 = wordRepository.findMember2();

        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setId(member.getId());
        memberResponse.setName(member.getName());
        memberResponse.setRegistrationId(member.getRegistrationId());

        return memberResponse;
    }

    public List<MemberResponse> testMultipleData() {
        MemberResponse member1 = new MemberResponse();
        member1.setName("John");

        MemberResponse member2 = new MemberResponse();
        member2.setName("Kim");

        ArrayList<MemberResponse> memberResponses = new ArrayList<>();
        memberResponses.add(member1);
        memberResponses.add(member2);
        return memberResponses;
    }

    /**
     *
     * @param word
     * @return
     */
    @Transactional
    public List<Map<String, Object>> createWordInfo(String word) {
        ObjectMapper objectMapper = new ObjectMapper();
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        WordOriginalData existedWord = wordRepository.findExistedWord(word);
        if (existedWord != null) {
            String jsonData = existedWord.getJsonString();
            return convertJsonToList(jsonData);
        }

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );

            List<Map<String, Object>> responseBody = response.getBody();

            String jsonString = objectMapper.writeValueAsString(responseBody);
            wordRepository.saveOriginalData(word, jsonString);

            return response.getBody();

        } catch (HttpClientErrorException e) {
            throw new BadRequestError("GPT한테 물어보고 적절히 예외처리 후 응답데이터 return 하기", e);
        } catch (HttpServerErrorException e) {
            throw new OpenApiServerError("open api 서버에 문제가 생겼음", e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 변환 중 오류 발생", e);
        }

    }

    public List<Map<String, Object>> convertJsonToList(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(jsonString, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON 파싱 중 오류 발생", e);
        }
    }


}
