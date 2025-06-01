package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record VocabularyReviewEndRequest(
        Long reviewScheduleId,
        List<WordAnswer> answers,
        int wordsMarkedKnown,

        int totalWordsInSession,
        int lastWordIndexSeen
        ) {
}
