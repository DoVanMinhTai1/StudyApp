package nlu.fit.studyappr.model.vocabulary; // Your package

import java.io.Serializable;
import java.util.List;

public class VocabularyReviewEndResponse implements Serializable {

    private String sessionId; // ID of the session that just ended
    private String reviewMethod; // e.g., "Flashcard", "Quiz"
    private int totalWordsReviewed; // Tổng số từ đã ôn
    private int wordsRemembered;    // Số từ ghi nhớ tốt (marked as KNOWN)
    private int wordsToReviewAgain; // Số từ cần ôn lại (marked as UNKNOWN)
    private String suggestionText;  // Text for the suggestion box
    private List<NextReviewTimeInfo> nextReviewTimes; // List of words and their next suggested review time

    // Constructors
    public VocabularyReviewEndResponse() {}

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getReviewMethod() { return reviewMethod; }
    public void setReviewMethod(String reviewMethod) { this.reviewMethod = reviewMethod; }

    public int getTotalWordsReviewed() { return totalWordsReviewed; }
    public void setTotalWordsReviewed(int totalWordsReviewed) { this.totalWordsReviewed = totalWordsReviewed; }

    public int getWordsRemembered() { return wordsRemembered; }
    public void setWordsRemembered(int wordsRemembered) { this.wordsRemembered = wordsRemembered; }

    public int getWordsToReviewAgain() { return wordsToReviewAgain; }
    public void setWordsToReviewAgain(int wordsToReviewAgain) { this.wordsToReviewAgain = wordsToReviewAgain; }

    public String getSuggestionText() { return suggestionText; }
    public void setSuggestionText(String suggestionText) { this.suggestionText = suggestionText; }

    public List<NextReviewTimeInfo> getNextReviewTimes() { return nextReviewTimes; }
    public void setNextReviewTimes(List<NextReviewTimeInfo> nextReviewTimes) { this.nextReviewTimes = nextReviewTimes; }
}