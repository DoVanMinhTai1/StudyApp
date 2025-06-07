package nlu.fit.studyappr.model.exam;

import java.util.Map;
import java.util.List;

public class Question {
    private String type;
    private int questionNumber;
    private String questionText;
    private String correctAnswer;
    private Map<String, String> options;
    private String passage;
    private List<SubQuestion> subQuestions;
    // nếu có dạng group

    public String getType() {
        return type;
    }

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

    public List<SubQuestion> getSubQuestions() {
        return subQuestions;
    }
    public String getPassage(){
        return passage;
    }
}
