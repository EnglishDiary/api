package org.eng_diary.api.business.word.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/word")
public class WordController {

    @GetMapping("/test")
    public String test() {
        return "헬로우";
    }

}
