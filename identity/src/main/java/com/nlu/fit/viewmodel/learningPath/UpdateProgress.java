package com.nlu.fit.viewmodel.learningPath;

public record UpdateProgress(
        String userId,
        Long topicId,
        String topicType, // "VOCABULARY" hoặc "GRAMMAR"
        String topicTitle,
        int timeSpentSeconds
) {
}
