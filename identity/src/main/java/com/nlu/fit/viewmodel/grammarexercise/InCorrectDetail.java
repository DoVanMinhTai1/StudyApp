package com.nlu.fit.viewmodel.grammarexercise;

import lombok.Builder;

@Builder
public record InCorrectDetail(
        Long id,
        String questionText,
        String userAnswer,
        String correctAnswer,
        String explanation
) {
}