package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record CompleteReviewRequest(
        String reviewScheduleId,
        String status,
        List<ReviewResults> reviewResults
) {
}
