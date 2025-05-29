package nlu.fit.studyappr.model;

public class GrammarReviewResult {
    private Long id;

    private int score;

    private String userId;

    private Long grammarTopic;

    public GrammarReviewResult(Long id, int score, String userId, Long grammarTopic) {
        this.id = id;
        this.score = score;
        this.userId = userId;
        this.grammarTopic = grammarTopic;
    }

    public GrammarReviewResult() {
    }

    @Override
    public String toString() {
        return "GrammarReviewResult{" +
                "id=" + id +
                ", score=" + score +
                ", userId=" + userId +
                ", grammarTopic=" + grammarTopic +
                '}';
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGrammarTopic() {
        return grammarTopic;
    }

    public void setGrammarTopic(Long grammarTopic) {
        this.grammarTopic = grammarTopic;
    }
}

