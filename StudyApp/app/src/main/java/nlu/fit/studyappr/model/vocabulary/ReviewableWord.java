package nlu.fit.studyappr.model.vocabulary;

import java.io.Serializable;

public class ReviewableWord implements Serializable {
    private Long id; // Or Long
    private String word;
    private String meaning;
    private String status;
    private String nextReviewTime;

    public ReviewableWord(Long id, String word, String meaning, String status, String nextReviewTime) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.status = status;
        this.nextReviewTime = nextReviewTime;
    }

    public Long getId() { return id; }
    public String getWord() { return word; }
    public String getMeaning() { return meaning; }
    public String getStatus() { return status; }
    public String getNextReviewTime() { return nextReviewTime; }
}
