package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record VocabularyEndRequest(
        String userId,
        Long sessionId,
        Long topicId,
        String topicType,
        List<WordAnswer> wordsLearnedInSession,
        int timeSpentSeconds
) {
}
