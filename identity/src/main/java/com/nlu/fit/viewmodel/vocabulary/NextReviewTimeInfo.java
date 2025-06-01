package com.nlu.fit.viewmodel.vocabulary;

public record NextReviewTimeInfo(
         String word,// The word itself
         String nextReviewTimeText // e.g., "Ngày mai", "Trong 3 ngày", "2025-06-05"

) {
}
