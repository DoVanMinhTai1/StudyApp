package nlu.fit.studyappr.model.grammar;

public class UserAnswer {
    private Long id;
    private Double score;

    private Long grammarExerciseQuestionId;

    private Long grammarReviewResultId;

    public UserAnswer(Long id, Double score, Long grammarExerciseQuestionId, Long grammarReviewResultId) {
        this.id = id;
        this.score = score;
        this.grammarExerciseQuestionId = grammarExerciseQuestionId;
        this.grammarReviewResultId = grammarReviewResultId;
    }

    public UserAnswer(Long grammarExerciseQuestionId, Double score, Long grammarReviewResultId) {
        this.grammarExerciseQuestionId = grammarExerciseQuestionId;
        this.score = score;
        this.grammarReviewResultId = grammarReviewResultId;
    }

    public UserAnswer() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Long getGrammarExerciseQuestionId() {
        return grammarExerciseQuestionId;
    }

    public void setGrammarExerciseQuestionId(Long grammarExerciseQuestionId) {
        this.grammarExerciseQuestionId = grammarExerciseQuestionId;
    }

    public Long getGrammarReviewResultId() {
        return grammarReviewResultId;
    }

    public void setGrammarReviewResultId(Long grammarReviewResultId) {
        this.grammarReviewResultId = grammarReviewResultId;
    }

    @Override
    public String toString() {
        return "UserAnswer{" +
                "id=" + id +
                ", selectedAnswer='" + score + '\'' +
                ", grammarExerciseQuestionId=" + grammarExerciseQuestionId +
                ", grammarReviewResultId=" + grammarReviewResultId +
                '}';
    }
}
