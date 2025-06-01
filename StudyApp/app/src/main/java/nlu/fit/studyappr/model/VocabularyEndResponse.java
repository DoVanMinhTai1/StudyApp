package nlu.fit.studyappr.model;

import java.io.Serializable;
import java.util.List;

public class VocabularyEndResponse implements Serializable {
    private String sessionId;
    private String topicTitle;
    private String level;
    private String method;
    private int totalWordsInSession;
    private int wordsCorrectOrLearned;
    private int wordsIncorrectOrPending;
    private String suggestion;
    private String nextStepPathId; // Optional

    // Constructors, Getters, Setters
    public VocabularyEndResponse() {}

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getTopicTitle() { return topicTitle; }
    public void setTopicTitle(String topicTitle) { this.topicTitle = topicTitle; }
    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public int getTotalWordsInSession() { return totalWordsInSession; }
    public void setTotalWordsInSession(int totalWordsInSession) { this.totalWordsInSession = totalWordsInSession; }
    public int getWordsCorrectOrLearned() { return wordsCorrectOrLearned; }
    public void setWordsCorrectOrLearned(int wordsCorrectOrLearned) { this.wordsCorrectOrLearned = wordsCorrectOrLearned; }
    public int getWordsIncorrectOrPending() { return wordsIncorrectOrPending; }
    public void setWordsIncorrectOrPending(int wordsIncorrectOrPending) { this.wordsIncorrectOrPending = wordsIncorrectOrPending; }
    public String getSuggestion() { return suggestion; }
    public void setSuggestion(String suggestion) { this.suggestion = suggestion; }
    public String getNextStepPathId() { return nextStepPathId; }
    public void setNextStepPathId(String nextStepPathId) { this.nextStepPathId = nextStepPathId; }
}
