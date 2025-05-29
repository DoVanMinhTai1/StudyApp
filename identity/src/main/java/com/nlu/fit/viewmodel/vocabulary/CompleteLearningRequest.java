package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record CompleteLearningRequest(
        String sessionId,
        List<LearningResults> learningResults
) {
}
