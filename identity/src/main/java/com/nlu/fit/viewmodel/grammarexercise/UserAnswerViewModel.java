package com.nlu.fit.viewmodel.grammarexercise;

import lombok.Builder;

@Builder
public record UserAnswerViewModel(
        Long id,
        Double score,
        Long grammarExerciseQuestionId,
        Long grammarReviewResultId
) {
}
