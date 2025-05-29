package com.nlu.fit.service;

import com.google.api.gax.rpc.NotFoundException;
import com.nlu.fit.model.GrammarExerciseQuestion;
import com.nlu.fit.model.GrammarReviewResult;
import com.nlu.fit.model.UserAnswer;
import com.nlu.fit.repository.GrammarExerciseQuestionRepository;
import com.nlu.fit.repository.GrammarReviewResultRepository;
import com.nlu.fit.repository.UserAnswerRepository;
import com.nlu.fit.viewmodel.grammarexercise.UserAnswerViewModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@AllArgsConstructor
public class UserAnswerService {

    private final UserAnswerRepository userAnswerRepository;
    private final GrammarExerciseQuestionRepository grammarExerciseQuestionRepository;
    private final GrammarReviewResultRepository grammarReviewResultRepository;

    public boolean saveUserAnswer(List<UserAnswer> userAnswers) {
        List<UserAnswer> savedAnswers = new ArrayList<>();
        for(UserAnswer userAnswer : userAnswers) {
            Long grammarExerciseQuestionId = userAnswer.getGrammarExerciseQuestion() != null ? userAnswer.getGrammarExerciseQuestion().getId() : null;
            Long grammarReviewResultId = userAnswer.getGrammarReviewResult() != null ? userAnswer.getGrammarReviewResult().getId() : null; 

            if (grammarExerciseQuestionId == null | grammarReviewResultId == null) {
                continue;
            }

            Optional<GrammarExerciseQuestion> grammarExerciseQuestion = grammarExerciseQuestionRepository.findById(grammarReviewResultId);

            Optional<GrammarReviewResult> grammarReviewResult = grammarReviewResultRepository.findById(grammarReviewResultId);

            userAnswer.setGrammarExerciseQuestion(grammarExerciseQuestion.get());
            userAnswer.setGrammarReviewResult(grammarReviewResult.get());

            savedAnswers.add(userAnswerRepository.save(userAnswer));
        }
        return !savedAnswers.isEmpty();
    }
}
