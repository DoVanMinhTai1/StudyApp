package nlu.fit.studyappr.model.vocabulary; // Your package

import java.io.Serializable;

public class NextReviewTimeInfo implements Serializable {
    private String word; // The word itself
    private String nextReviewTimeText; // e.g., "Ngày mai", "Trong 3 ngày", "2025-06-05"

    // Constructors
    public NextReviewTimeInfo() {}

    public NextReviewTimeInfo(String word, String nextReviewTimeText) {
        this.word = word;
        this.nextReviewTimeText = nextReviewTimeText;
    }

    // Getters and Setters
    public String getWord() { return word; }
    public void setWord(String word) { this.word = word; }

    public String getNextReviewTimeText() { return nextReviewTimeText; }
    public void setNextReviewTimeText(String nextReviewTimeText) { this.nextReviewTimeText = nextReviewTimeText; }
}