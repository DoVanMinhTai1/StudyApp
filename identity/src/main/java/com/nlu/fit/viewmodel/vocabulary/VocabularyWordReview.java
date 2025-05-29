package com.nlu.fit.viewmodel.vocabulary;

import lombok.Builder;

import java.util.List;

@Builder
public record VocabularyWordReview(
        int totalWords,
        List<VocabularyWordsReviewList> vocabularyWords
) {
}
