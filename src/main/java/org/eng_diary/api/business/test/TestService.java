package org.eng_diary.api.business.test;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.test.entity.Mapping;
import org.eng_diary.api.business.test.entity.Server;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TestQueryRepository testQueryRepository;

    public String test() {
        List<Server> server = testQueryRepository.findServer();
        System.out.println("디버깅");

//        List<Mapping> mapping = testQueryRepository.findMapping();
//        System.out.println("디버깅");

        return null;
    }
}
