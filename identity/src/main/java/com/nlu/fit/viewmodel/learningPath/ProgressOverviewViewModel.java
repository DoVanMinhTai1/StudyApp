package com.nlu.fit.viewmodel.learningPath;

import lombok.Builder;

@Builder
public record ProgressOverviewViewModel(
        int grammarProgress,
        int vocabularyProgress,
        int learningPathProgress
) {
}
