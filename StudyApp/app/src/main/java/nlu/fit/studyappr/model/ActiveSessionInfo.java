package nlu.fit.studyappr.model;

import java.io.Serializable;

public class ActiveSessionInfo implements Serializable {
    private String sessionId;
    private String topicId; // Or Long
    private String topicName;
    private int wordsCompletedInSession;
    private int totalWordsInSession;
    private int lastSeenWordIndex;
    private String reviewMethod;

    // Getters and Setters
    public String getSessionId() { return sessionId; }
    public String getTopicId() { return topicId; }
    public String getTopicName() { return topicName; }
    public int getWordsCompletedInSession() { return wordsCompletedInSession; }
    public int getTotalWordsInSession() { return totalWordsInSession; }
    public int getLastSeenWordIndex() { return lastSeenWordIndex; }
    public String getReviewMethod() { return reviewMethod; }
}