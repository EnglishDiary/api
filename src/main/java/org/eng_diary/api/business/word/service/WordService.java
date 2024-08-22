package org.eng_diary.api.business.word.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.repository.WordRepository;
import org.eng_diary.api.domain.*;
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

// TODO 240822 체크예외 관련 로직은 유틸 클래스 메소드로 따로 뺀 뒤 거기서 try-catch 처리하기
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

    @Transactional
    public void saveWordInfo(String word, String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();

        // TODO: 언제 생성됐는지랑 수정됐는지 + 뜻하고 예문들 순서
        try {
            JsonNode wordData = objectMapper.readTree(jsonData);

            MemberWord memberWord = new MemberWord();
            Member member = new Member();
            member.setId(1L);

            memberWord.setWord(word);
            memberWord.setMember(member);

            wordRepository.saveMemberWord(memberWord);

            JsonNode meanings = wordData.get("meanings");
            for (JsonNode meaning: meanings) {
                MemberWordKind kind = new MemberWordKind();
                kind.setPartOfSpeech(meaning.get("partOfSpeech").asText());
                kind.setMemberWord(memberWord);
                wordRepository.saveMemberWordKind(kind);

                JsonNode definitions = meaning.get("definitions");
                for (JsonNode definition : definitions) {
                    MemberWordMeaning wordMeaning = new MemberWordMeaning();
                    wordMeaning.setDefinition(definition.get("definition").asText());
                    wordMeaning.setKind(kind);
                    wordRepository.saveMemberWordMeaning(wordMeaning);

                    if (definition.has("example")) {
                        MemberWordExample memberWordExample = new MemberWordExample();
                        memberWordExample.setExample(definition.get("example").asText());
                        memberWordExample.setMeaning(wordMeaning);
                        wordRepository.saveMemberWordExample(memberWordExample);
                    }

                    if (definition.has("userExamples")) {
                        JsonNode userExamples = definition.get("userExamples");
                        for (JsonNode userExample : userExamples) {
                            MemberWordExample example = new MemberWordExample();
                            example.setExample(userExample.asText());
                            example.setMeaning(wordMeaning);
                            wordRepository.saveMemberWordExample(example);
                        }
                    }
                }


            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}
