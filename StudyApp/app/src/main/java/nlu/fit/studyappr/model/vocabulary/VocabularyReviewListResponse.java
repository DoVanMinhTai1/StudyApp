package nlu.fit.studyappr.model.vocabulary;

import java.io.Serializable;
import java.util.List;

public class VocabularyReviewListResponse implements Serializable {
    private ActiveSessionInfo activeSession; // Can be null
    private int totalWordsToReviewCount;
    private List<ReviewableWord> wordsToReview;


    // Getters and Setters
    public ActiveSessionInfo getActiveSession() { return activeSession; }
    public List<ReviewableWord> getWordsToReview() { return wordsToReview; }
    public int getTotalWordsToReviewCount() { return totalWordsToReviewCount; }
}
