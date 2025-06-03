package nlu.fit.studyappr.model.vocabulary;

import java.io.Serializable;

public class WordAnswer implements Serializable {
    private Long wordId;
    private Boolean status;

    private String userAnswer;

    public WordAnswer(Long wordId, Boolean status, String userAnswer) {
        this.wordId = wordId;
        this.status = status;
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public Long getWordId() {
        return wordId;
    }

    public Boolean getStatus() {
        return status;
    }
}
