package org.eng_diary.api.business.word.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.repository.WordRepository;
import org.eng_diary.api.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WordService {
    private final WordRepository wordRepository;

    public MemberResponse testService() {
        Member member = wordRepository.findMember();
        Member member2 = wordRepository.findMember2();

        MemberResponse memberResponse = new MemberResponse();
        memberResponse.setId(member.getId());
        memberResponse.setName(member.getName());
        memberResponse.setRegistrationId(member.getRegistrationId());

        return memberResponse;
    }
}
