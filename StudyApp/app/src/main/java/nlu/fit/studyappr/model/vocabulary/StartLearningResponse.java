package nlu.fit.studyappr.model.vocabulary;

import java.io.Serializable;
import java.util.List;

public class StartLearningResponse implements Serializable {
    private Long sessionId;
    String method;
    List<VocabularyWordViewModel> words;

    Long topicId;

    String title;
    public StartLearningResponse() {
    }

    public StartLearningResponse(Long sessionId, String method, List<VocabularyWordViewModel> words) {
        this.sessionId = sessionId;
        this.method = method;
        this.words = words;
    }

    public StartLearningResponse(Long sessionId, String method, List<VocabularyWordViewModel> words, Long topicId) {
        this.sessionId = sessionId;
        this.method = method;
        this.words = words;
        this.topicId = topicId;
    }

    public StartLearningResponse(Long sessionId, String method, List<VocabularyWordViewModel> words, Long topicId, String title) {
        this.sessionId = sessionId;
        this.method = method;
        this.words = words;
        this.topicId = topicId;
        this.title = title;
    }

    @Override
    public String toString() {
        return "StartLearningResponse{" +
                "sessionId=" + sessionId +
                ", method='" + method + '\'' +
                ", words=" + words +
                '}';
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
