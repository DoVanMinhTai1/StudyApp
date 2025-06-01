package com.nlu.fit.viewmodel.vocabulary;

import lombok.Builder;

@Builder
public record VocabularyEndResponse(
         Long sessionId,
         String topicTitle,
         String level,
         String method,
         int totalWordsInSession,
         int wordsCorrectOrLearned,
         int wordsIncorrectOrPending,
         String suggestion) {
}
