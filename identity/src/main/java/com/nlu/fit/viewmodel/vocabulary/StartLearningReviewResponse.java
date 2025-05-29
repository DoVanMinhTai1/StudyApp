package com.nlu.fit.viewmodel.vocabulary;

import lombok.Builder;

import java.util.List;

@Builder
public record StartLearningReviewResponse(
        Long reviewScheduleId,
        String method,
        List<VocabularyWordViewModel> words
) {
}
