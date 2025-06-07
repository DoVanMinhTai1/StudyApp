package com.nlu.fit.viewmodel.vocabulary;

import lombok.Builder;

import java.util.List;

@Builder
public record VocabularyReviewListResponse(
        ActiveSessionInfo activeSessionInfo,
        int totalWordsToReviewCount,
        List<VocabularyWordsReviewList> wordsToReview
) {
}
