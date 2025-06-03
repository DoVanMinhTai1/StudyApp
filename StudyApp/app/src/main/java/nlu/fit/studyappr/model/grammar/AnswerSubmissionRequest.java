package nlu.fit.studyappr.model.grammar;

public class AnswerSubmissionRequest {
    Long id;
    String selectedAnswer;

    public AnswerSubmissionRequest(Long id, String selectedAnswer) {
        this.id = id;
        this.selectedAnswer = selectedAnswer;
    }

    public AnswerSubmissionRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    @Override
    public String toString() {
        return "AnswerSubmissionRequest{" +
                "id=" + id +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                '}';
    }
}
