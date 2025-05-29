package com.nlu.fit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nlu.fit.model.GrammarExerciseQuestion;
import com.nlu.fit.model.GrammarReviewResult;
import com.nlu.fit.model.GrammarTopic;
import com.nlu.fit.repository.GrammarExerciseQuestionRepository;
import com.nlu.fit.repository.GrammarLessonRepository;
import com.nlu.fit.repository.GrammarReviewResultRepository;
import com.nlu.fit.repository.GrammarTopicRepository;
import com.nlu.fit.viewmodel.grammarexercise.AnswerSubmissionRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GrammarReviewService {

    private GrammarReviewResultRepository grammarReviewResultRepository;
    private GrammarTopicRepository grammarTopicRepository;
    private GrammarLessonRepository grammarLessonRepository;
    private GrammarExerciseQuestionRepository grammarExerciseQuestionRepository;

    public List<GrammarTopic> getAllTopicLearned(String uuId) {
        String userId = uuId;

        List<GrammarReviewResult> grammarReviewResult = grammarReviewResultRepository.findAllByUserId(userId);
        List<Long> grammarTopicList = new ArrayList<>();

        grammarReviewResult.stream().map(item -> grammarTopicList.add(item.getGrammarTopic().getId()))
                .distinct().collect(Collectors.toList());

        List<GrammarTopic> result = grammarTopicRepository.findAllById(grammarTopicList);

        return result;

    }

    public List<GrammarExerciseQuestion> getListQuestionByTopicId(Long topicId) {

        var lesson = grammarLessonRepository.findGrammarLessonByGrammar_topic(topicId);
        if (lesson == null) {
            throw new IllegalArgumentException("No lesson found for topic ID: " + topicId);
        }
        Long lessonId = lesson.getId();

        List<GrammarExerciseQuestion> result = grammarExerciseQuestionRepository.findAllByGrammarlesson(lessonId);

        return result;
    }
}
