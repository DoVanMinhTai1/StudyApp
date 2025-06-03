package nlu.fit.studyappr.model.grammar;

public class InCorrectDetail {
    Long id;
    String questionText;
    String userAnswer;
    String correctAnswer;
    String explanation;

    public InCorrectDetail() {
    }

    public InCorrectDetail(Long id, String questionText, String userAnswer, String correctAnswer, String explanation) {
        this.id = id;
        this.questionText = questionText;
        this.userAnswer = userAnswer;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "InCorrectDetail{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", userAnswer='" + userAnswer + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
