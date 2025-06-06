package com.nlu.fit.viewmodel.learningPath;

public record LearningPathRequest(
        String targetScore,
        String studyDuration,
        int hoursPerWeek
) {
}
