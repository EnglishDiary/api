package org.eng_diary.api.business.word.service;

import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.repository.WordRepository;
import org.eng_diary.api.domain.Member;
import org.eng_diary.api.dto.WordBasicInfo;
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
    public WordBasicInfo createWordInfo(String word) {
        String apiUrl = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            List<Map<String, Object>> responseList = response.getBody();
            Map<String, Object> wordDetail = responseList.get(0);

            WordBasicInfo wordBasicInfo = new WordBasicInfo();

            String phonetic = (String) wordDetail.get("phonetic");
            List<Map<String, Object>> phonetics = (List<Map<String, Object>>) wordDetail.get("phonetics");
            Map<String, Object> phoneticMap = phonetics.get(0);

            wordBasicInfo.setPhonetic(phonetic);
            wordBasicInfo.setEtc("기타 등등 ..");

            return wordBasicInfo;

        } catch (HttpClientErrorException e) {
            throw new BadRequestError("클로드한테 물어보고 적절히 예외처리 후 응답데이터 return 하기");
        } catch (HttpServerErrorException e) {
            throw new OpenApiServerError("open api 서버에 문제가 생겼음");
        }

    }

}
