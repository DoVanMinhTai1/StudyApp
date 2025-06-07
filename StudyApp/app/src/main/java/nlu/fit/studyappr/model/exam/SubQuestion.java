package nlu.fit.studyappr.model.exam;

import java.util.Map;

public class SubQuestion {
    private int questionNumber;
    private String questionText;
    private String correctAnswer;
    private Map<String, String> options;

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public Map<String, String> getOptions() {
        return options;
    }
}
