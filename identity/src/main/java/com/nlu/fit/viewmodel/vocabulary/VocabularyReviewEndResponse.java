package com.nlu.fit.viewmodel.vocabulary;

import java.util.List;

public record VocabularyReviewEndResponse(
         String sessionId,// ID of the session that just ended
         String reviewMethod, // e.g., "Flashcard", "Quiz"
         int totalWordsReviewed, // Tổng số từ đã ôn
         int wordsRemembered,   // Số từ ghi nhớ tốt (marked as KNOWN)
         int wordsToReviewAgain, // Số từ cần ôn lại (marked as UNKNOWN)
         String suggestionText,  // Text for the suggestion box
         List<NextReviewTimeInfo> nextReviewTimes // List of words and their next suggested review time

) {
}
