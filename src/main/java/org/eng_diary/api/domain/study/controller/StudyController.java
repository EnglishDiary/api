package org.eng_diary.api.domain.study.controller;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.domain.study.dto.request.AiAskingForm;
import org.eng_diary.api.domain.study.dto.request.ChapterSaveForm;
import org.eng_diary.api.domain.study.dto.request.ScriptUploadForm;
import org.eng_diary.api.domain.study.dto.request.TopicSaveForm;
import org.eng_diary.api.domain.study.dto.response.*;
import org.eng_diary.api.domain.study.service.AiService;
import org.eng_diary.api.domain.study.service.StudyService;
import org.eng_diary.api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {

    private final AiService aiService;
    private final StudyService studyService;

    @PostMapping("/ai/asking")
    public ResponseEntity<ApiResponse<AiAnswerRes>> askAiSentence(@RequestBody AiAskingForm aiAskingForm) {
        return ApiResponse.success(aiService.askAiSentence(aiAskingForm));
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicRes>>> getTopics() {
        return ApiResponse.success(studyService.getTopics());
    }

    @PostMapping("/topic")
    public ResponseEntity<ApiResponse<TopicRes>> saveTopic(@RequestBody TopicSaveForm topicSaveForm) {
        return ApiResponse.success(studyService.saveTopic(topicSaveForm));
    }

    @GetMapping("/topic/{topicId}/chapters")
    public ResponseEntity<ApiResponse<List<ChapterRes>>> getChapters(@PathVariable(name = "topicId") Long topicId) {
        return ApiResponse.success(studyService.getChapters(topicId));
    }

    @PostMapping("/topic/{topicId}/chapter")
    public ResponseEntity<ApiResponse<ChapterRes>> saveChapter(
            @PathVariable(name = "topicId") Long topicId,
            @RequestBody ChapterSaveForm chapterSaveForm) {
        return ApiResponse.success(studyService.saveChapter(topicId, chapterSaveForm));
    }

    @PostMapping("/topic/{topicId}/chapter/{chapterId}/script")
    public ResponseEntity<ApiResponse<Long>> uploadScript(
            @PathVariable(name = "topicId") Long topicId,
            @PathVariable(name = "chapterId") Long chapterId,
            @RequestBody ScriptUploadForm scriptUploadForm) {
        return ApiResponse.success(studyService.uploadScript(chapterId, scriptUploadForm));
    }

    @GetMapping("/topic/{topicId}/chapter/{chapterId}/script/{scriptId}")
    public ResponseEntity<ApiResponse<ScriptRes>> getScript(@PathVariable(name = "scriptId") Long scriptId) {
        return ApiResponse.success(studyService.getScript(scriptId));
    }

    @GetMapping("/sentence/{sentenceId}/conversations")
    public ResponseEntity<ApiResponse<List<ConversationRes>>> getConversations(
            @PathVariable(name = "sentenceId") Long sentenceId) {
        return ApiResponse.success(studyService.getConversations(sentenceId));
    }

    @PostMapping("/chapter/{chapterId}/bookmark/{bookmarkIndex}")
    public ResponseEntity<ApiResponse<Integer>> saveBookmark(
            @PathVariable(name = "chapterId") Long chapterId,
            @PathVariable(name = "bookmarkIndex") Integer bookmarkIndex) {
        return ApiResponse.success(studyService.saveBookmark(chapterId, bookmarkIndex));
    }

    @GetMapping("/chapter/{chapterId}")
    public ResponseEntity<ApiResponse<ChapterRes>> getChapter(@PathVariable(name = "chapterId") Long chapterId) {
        return ApiResponse.success(studyService.getChapter(chapterId));
    }

}
