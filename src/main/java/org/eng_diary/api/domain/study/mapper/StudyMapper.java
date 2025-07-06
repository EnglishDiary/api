package org.eng_diary.api.domain.study.mapper;

import org.eng_diary.api.domain.study.dto.request.AiAskingForm;
import org.eng_diary.api.domain.study.dto.request.ChapterSaveForm;
import org.eng_diary.api.domain.study.dto.request.ScriptUploadForm;
import org.eng_diary.api.domain.study.dto.request.TopicSaveForm;
import org.eng_diary.api.domain.study.dto.response.*;
import org.eng_diary.api.domain.study.entity.*;
import org.eng_diary.api.entity.Member;

import java.util.List;
import java.util.Optional;

public class StudyMapper {

    public static TopicRes createTopicRes(Topic topic) {
        return TopicRes.builder()
                .id(topic.getId())
                .name(topic.getName())
                .desc(topic.getDesc())
                .build();
    }

    public static Topic createTopic(TopicSaveForm form, Member currentUser) {
        return Topic.builder()
                .name(form.name())
                .desc(form.desc())
                .member(currentUser)
                .build();
    }

    public static ChapterRes createChapterRes(Chapter chapter) {
        return ChapterRes.builder()
                .id(chapter.getId())
                .name(chapter.getName())
                .desc(chapter.getDesc())
                .scriptId(Optional.ofNullable(chapter.getScript())
                        .map(Script::getId)
                        .orElse(null))
                .build();
    }


    public static Chapter createChapter(ChapterSaveForm chapterSaveForm, Topic topic) {
        return Chapter.builder()
                .name(chapterSaveForm.name())
                .desc(chapterSaveForm.desc())
                .topic(topic)
                .build();
    }

    public static Script createScript(ScriptUploadForm scriptUploadForm) {
        return Script.builder()
                .content(scriptUploadForm.script())
                .desc(scriptUploadForm.desc())
                .build();
    }

    public static ScriptRes createScriptRes(Script script) {
        List<SentenceRes> sentences = script.getSentences()
                .stream()
                .map(StudyMapper::createSentenceRes)
                .toList();

        return ScriptRes.builder()
                .content(script.getContent())
                .sentences(sentences)
                .build();
    }

    public static SentenceRes createSentenceRes(Sentence sentence) {
        return SentenceRes.builder()
                .id((sentence.getId()))
                .passage(sentence.getPassage())
                .build();
    }

    public static Conversation createConversation(AiAskingForm aiAskingForm, String aiAnswer, Sentence sentence) {
        return Conversation.builder()
                .userQuestion(aiAskingForm.question())
                .aiAnswer(aiAnswer)
                .sentence(sentence)
                .build();
    }

    public static ConversationRes createConversationRes(Conversation conversation) {
        return ConversationRes.builder()
                .id(conversation.getId())
                .userQuestion(conversation.getUserQuestion())
                .aiAnswer(conversation.getAiAnswer())
                .build();
    }


}
