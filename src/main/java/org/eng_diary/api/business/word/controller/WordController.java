package org.eng_diary.api.business.word.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.word.dto.MemberResponse;
import org.eng_diary.api.business.word.service.WordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/word")
@RequiredArgsConstructor
public class WordController {

    private final WordService wordService;

    @GetMapping("/test")
    public Object test() {
        MemberResponse memberResponse = wordService.testService();

        return memberResponse;
    }


}
