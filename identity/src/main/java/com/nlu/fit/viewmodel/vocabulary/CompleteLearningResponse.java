package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record CompleteLearningResponse(
        int totalWords,
        int rememberedWords,
        List<VocabularyReviewResult> vocabularyReviewResults
) {
}
