package org.eng_diary.api.business.study.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.study.entity.*;
import org.eng_diary.api.business.study.repository.*;
import org.eng_diary.api.common.context.UserContext;
import org.eng_diary.api.common.context.UserContextHolder;
import org.eng_diary.api.business.auth.service.AuthService;
import org.eng_diary.api.business.study.dto.request.ChapterSaveForm;
import org.eng_diary.api.business.study.dto.request.ScriptUploadForm;
import org.eng_diary.api.business.study.dto.request.TopicSaveForm;
import org.eng_diary.api.business.study.dto.response.ChapterRes;
import org.eng_diary.api.business.study.dto.response.ConversationRes;
import org.eng_diary.api.business.study.dto.response.ScriptRes;
import org.eng_diary.api.business.study.dto.response.TopicRes;
import org.eng_diary.api.business.study.mapper.StudyMapper;
import org.eng_diary.api.business.auth.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyQueryRepository studyQueryRepository;
    private final TopicRepository topicRepository;
    private final AuthService authService;
    private final ChapterRepository chapterRepository;
    private final ScriptRepository scriptRepository;
    private final SentenceRepository sentenceRepository;
    private final ConversationRepository conversationRepository;

    @Transactional
    public Long uploadScript(Long chapterId, ScriptUploadForm scriptUploadForm) {
        Script script = StudyMapper.createScript(scriptUploadForm);
        scriptRepository.save(script);

        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("not existed chapter"));
        chapter.updateScript(script);

        List<Sentence> sentences = scriptUploadForm.sentences().stream()
                .map((sentence) -> Sentence.builder()
                        .passage(sentence)
                        .script(script)
                        .build())
                .toList();
        sentenceRepository.saveAll(sentences);

        return script.getId();
    }

    public List<TopicRes> getTopics() {
        UserContext userContext = UserContextHolder.getUserContext();
        List<Topic> topics = studyQueryRepository.findTopics(userContext.memberId());

        return topics.stream()
                .map(StudyMapper::createTopicRes)
                .toList();
    }

    @Transactional
    public TopicRes saveTopic(TopicSaveForm topicSaveForm) {
        Member currentUser = authService.getCurrentUser();

        Topic topic = StudyMapper.createTopic(topicSaveForm, currentUser);
        topicRepository.save(topic);

        return StudyMapper.createTopicRes(topic);
    }

    public List<ChapterRes> getChapters(Long topicId) {
        List<Chapter> chapters = studyQueryRepository.findChaptersByTopic(topicId);

        return chapters.stream()
                .map(StudyMapper::createChapterWithScriptRes)
                .toList();
    }

    public List<ChapterRes> getAllChapters() {
        List<Chapter> chapters = chapterRepository.findAll();

        return chapters.stream()
                .map(StudyMapper::createChapterRes)
                .toList();
    }

    @Transactional
    public ChapterRes saveChapter(Long topicId, ChapterSaveForm chapterSaveForm) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("not existed topic"));

        Chapter chapter = StudyMapper.createChapter(chapterSaveForm, topic);
        chapterRepository.save(chapter);

        return StudyMapper.createChapterWithScriptRes(chapter);
    }

    public ScriptRes getScript(Long scriptId) {
        Script script = studyQueryRepository.findScript(scriptId);

        return StudyMapper.createScriptRes(script);
    }

    public List<ConversationRes> getConversations(Long sentenceId) {
        Sentence sentence = sentenceRepository.findByIdOrThrow(sentenceId);

        List<Conversation> conversations = conversationRepository.findAllBySentence(sentence);
        return conversations.stream()
                .map(StudyMapper::createConversationRes)
                .toList();
    }


    @Transactional
    public Integer saveBookmark(Long chapterId, Integer bookmarkIndex) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("not existed chapter"));

        chapter.updateBookmark(bookmarkIndex);
        return bookmarkIndex;
    }

    public ChapterRes getChapter(Long chapterId) {
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("not existed chapter"));

        return StudyMapper.createChapterWithScriptRes(chapter);
    }
}
