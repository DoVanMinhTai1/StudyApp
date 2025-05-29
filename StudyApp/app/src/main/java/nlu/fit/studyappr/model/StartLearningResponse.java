package nlu.fit.studyappr.model;

import java.io.Serializable;
import java.util.List;

public class StartLearningResponse implements Serializable {
    private Long sessionId;
    String method;
    List<VocabularyWordViewModel> words;

    public StartLearningResponse() {
    }

    public StartLearningResponse(Long sessionId, String method, List<VocabularyWordViewModel> words) {
        this.sessionId = sessionId;
        this.method = method;
        this.words = words;
    }

    @Override
    public String toString() {
        return "StartLearningResponse{" +
                "sessionId=" + sessionId +
                ", method='" + method + '\'' +
                ", words=" + words +
                '}';
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<VocabularyWordViewModel> getWords() {
        return words;
    }

    public void setWords(List<VocabularyWordViewModel> words) {
        this.words = words;
    }
}
