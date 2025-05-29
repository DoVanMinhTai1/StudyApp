package com.nlu.fit.service.vocabulary;

import com.nlu.fit.enumeration.LearningStatusVocabulary;
import com.nlu.fit.enumeration.LevelTypeVocabulary;
import com.nlu.fit.model.vocabulary.LearningSession;
import com.nlu.fit.model.vocabulary.LearningSessionWord;
import com.nlu.fit.model.vocabulary.VocabularyTopic;
import com.nlu.fit.model.vocabulary.VocabularyWord;
import com.nlu.fit.repository.vocabulary.LearningSessionRepository;
import com.nlu.fit.repository.vocabulary.LearningSessionWordRepository;
import com.nlu.fit.repository.vocabulary.VocabularyTopicRepository;
import com.nlu.fit.repository.vocabulary.VocabularyWordRepository;
import com.nlu.fit.viewmodel.vocabulary.*;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class VocabularyService {
    private final VocabularyTopicRepository vocabularyTopicRepository;
    private final VocabularyWordRepository vocabularyWordRepository;
    private final LearningSessionRepository learningSessionRepository;
    private final LearningSessionWordRepository learningSessionWordRepository;

    public List<VocabularyTopic> getAllVocabularyTopics() {
        return vocabularyTopicRepository.findAll();
    }

    public List<Map<String, String>> getLevels() {
        List<VocabularyWord> vocabularyWordList = vocabularyWordRepository.findAll();
        return vocabularyWordList.stream().map(map -> {
            Map<String, String> result = new HashMap<>();
            result.put("id", String.valueOf(map.getId()));
            result.put("level", String.valueOf(map.getLevelTypeVocabulary().name()));
            return result;
        }).collect(Collectors.toList());
    }


    public StartLearningResponse startLearningRequest(StartLearningRequest request) {
        Long topicId = request.topicId();
        String level = request.level();
        String method = request.method();

        VocabularyTopic topic = vocabularyTopicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found"));

        LevelTypeVocabulary levelEnum;
        try {
            levelEnum = LevelTypeVocabulary.valueOf(level.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid level: " + level);
        }

        LearningSession learningSession = new LearningSession();
        learningSession.setUserId(getCurrentUser());
        learningSession.setLevelTypeVocabulary(levelEnum);
        learningSession.setMethod(method);
        learningSession.setStartTime(LocalDateTime.now());
        learningSession.setEndTime(null);
        learningSession.setVocabularyTopic(topic);

        learningSessionRepository.save(learningSession);

        List<VocabularyWord> words = vocabularyWordRepository.findAllByVocabularyTopic_Id(topicId)
                .stream().filter(word -> word.getLevelTypeVocabulary() == levelEnum).toList();

        List<LearningSessionWord> learningSessionWords = words.stream().map(item -> {
            LearningSessionWord learningSessionWord = new LearningSessionWord();
            learningSessionWord.setUserId(getCurrentUser());
            learningSessionWord.setAttemptAt(LocalDateTime.now());
            learningSessionWord.setStatus(LearningStatusVocabulary.NEW);
            learningSessionWord.setVocabularyWord(item);
            learningSessionWord.setLearningSession(learningSession);
            return learningSessionWord;
        }).collect(Collectors.toList());

        learningSessionWordRepository.saveAll(learningSessionWords);
        List<VocabularyWordViewModel> wordViewModels = words.stream().map(item -> {
            List<VocabularyWordViewModel> result = new ArrayList<>();
            result.add(VocabularyWordViewModel.builder().id(item.getId()).word(item.getWord()).meaning(item.getMeaning()).levelTypeVocabulary(String.valueOf(item.getLevelTypeVocabulary()))
                    .imageUrl(item.getImageUrl())
                    .audioUrl(item.getAudioUrl()).build());
            return result;
        }).flatMap(Collection::stream).collect(Collectors.toList());
        return StartLearningResponse.builder().sessionId(learningSession.getId()).method(method).words(wordViewModels).build();
    }

    private String getCurrentUser() {
        String result = SecurityContextHolder.getContext().getAuthentication().getName();
        return result;
    }


    public void completeLearning(CompleteLearningRequest request) {
        Long sessionId = Long.valueOf(request.sessionId());
        List<LearningResults> list = request.learningResults();

        LearningSession learningSession = learningSessionRepository
                .findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        for (LearningResults learningResult : list) {
            Optional<LearningSessionWord> optionalLSW = learningSessionWordRepository
                    .findByLearningSession_IdAndVocabularyWord_Id(sessionId, learningResult.vocabularyWordId());

            LearningSessionWord lsw = optionalLSW.orElseThrow(() ->
                    new RuntimeException("LearningSessionWord not found for wordId: " + learningResult.vocabularyWordId())
            );

            lsw.setStatus(LearningStatusVocabulary.valueOf(learningResult.status()));
            lsw.setAttemptAt(LocalDateTime.now());
            learningSessionWordRepository.save(lsw);
        }

        learningSession.setEndTime(LocalDateTime.now());
        learningSessionRepository.save(learningSession);
    }

}
