package org.eng_diary.api.business.exam.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.exam.payload.ExamSentenceDTO;
import org.eng_diary.api.business.exam.payload.ExamSentencesRequest;
import org.eng_diary.api.business.exam.repository.ExamRepository;
import org.eng_diary.api.domain.ExamSentence;
import org.eng_diary.api.domain.ExamSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    public List<ExamSentenceDTO> getSentences(ExamSentencesRequest request) {
//        ExamSet examSet = examRepository.findExamSet(request);
        List<ExamSentence> sentences = examRepository.findSentences(request);

        return sentences.stream().map((item) -> {
            ExamSentenceDTO dto = new ExamSentenceDTO();
            dto.setId(item.getId());
            dto.setSourceSentence(item.getSourceSentence());
            dto.setTranslation(item.getTranslation());
            dto.setLevel(item.getLevel());
            return dto;
        }).collect(Collectors.toList());

    }
}
