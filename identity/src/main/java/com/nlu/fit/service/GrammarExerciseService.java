package com.nlu.fit.service;

import com.google.api.gax.rpc.NotFoundException;
import com.nlu.fit.model.GrammarExerciseQuestion;
import com.nlu.fit.model.GrammarReviewResult;
import com.nlu.fit.model.GrammarTopic;
import com.nlu.fit.repository.GrammarExerciseQuestionRepository;
import com.nlu.fit.repository.GrammarReviewResultRepository;
import com.nlu.fit.viewmodel.grammarexercise.AnswerSubmissionRequest;
import com.nlu.fit.viewmodel.grammarexercise.ExerciseResult;
import com.nlu.fit.viewmodel.grammarexercise.InCorrectDetail;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GrammarExerciseService {
    private final GrammarExerciseQuestionRepository grammarExerciseQuestionRepository;
    private final GrammarReviewResultRepository grammarReviewResultRepository;

    public ExerciseResult checkAnswer(List<AnswerSubmissionRequest> questions) {
        List<Long> ids = questions.stream().map(AnswerSubmissionRequest::id).toList();
        List<GrammarExerciseQuestion> grammarExerciseQuestionsList = grammarExerciseQuestionRepository.findAllById(ids);

        Map<Long, String> userAnswersMap = questions.stream()
                .collect(Collectors.toMap(AnswerSubmissionRequest::id, AnswerSubmissionRequest::selectedAnswer));

        int correct = 0;
        List<InCorrectDetail> inCorrectDetails = new ArrayList<>();

        for (GrammarExerciseQuestion grammarExerciseQuestion : grammarExerciseQuestionsList) {
            String userAnswer = userAnswersMap.get(grammarExerciseQuestion.getId());

            if (grammarExerciseQuestion.getCorrectAnswer().equalsIgnoreCase(userAnswer)) {
                correct++;
            } else {
                InCorrectDetail inCorrectDetail = new InCorrectDetail(grammarExerciseQuestion.getId(),
                        grammarExerciseQuestion.getQuestionText(), userAnswer,
                        grammarExerciseQuestion.getCorrectAnswer(), grammarExerciseQuestion.getExplanation());
                inCorrectDetails.add(inCorrectDetail);
            }

        }

        return new ExerciseResult(correct, grammarExerciseQuestionsList.size(), inCorrectDetails);
    }

    public GrammarReviewResult saveGrammarReviewResult(GrammarReviewResult grammarReviewResult) {
        Long grammarTopicId = grammarReviewResultRepository.findById(grammarReviewResult.getGrammarTopic().getId())
                .get().getId();
        if (grammarTopicId == null) {
            throw new NotFoundException("Not Found Exception", null, null, false);
        }
        return grammarReviewResultRepository.save(grammarReviewResult);
    }
}
