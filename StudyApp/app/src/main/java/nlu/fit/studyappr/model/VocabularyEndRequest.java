package nlu.fit.studyappr.model;

import java.io.Serializable;
import java.util.List;

public class VocabularyEndRequest implements Serializable {
    private String userId;
    private Long sessionId;
    private Long topicId;
    private String topicType;
    private Integer lastWordIndexSeen;
    private List<WordAnswer> wordsLearnedInSession;
    private int timeSpentSeconds;
    private Integer scoreAchieved;

    private Integer totalQuestionsInSession;

    // Constructor, Getters, Setters
    public VocabularyEndRequest(String userId, Long topicId, String topicType, int timeSpentSeconds, Long currentSessionId) {
        this.userId = userId;
        this.topicId = topicId;
        this.topicType = topicType;
        this.timeSpentSeconds = timeSpentSeconds;
        this.sessionId = currentSessionId;
    }
    // Add other fields to constructor as needed, or use setters

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public Long getSessionId() { return sessionId; }
    public void setSessionId(Long sessionId) { this.sessionId = sessionId; }
    public Long getTopicId() { return topicId; }
    public void setTopicId(Long topicId) { this.topicId = topicId; }
    public String getTopicType() { return topicType; }
    public void setTopicType(String topicType) { this.topicType = topicType; }
    public Integer getLastWordIndexSeen() { return lastWordIndexSeen; }
    public void setLastWordIndexSeen(Integer lastWordIndexSeen) { this.lastWordIndexSeen = lastWordIndexSeen; }
    public List<WordAnswer> getWordsLearnedInSession() { return wordsLearnedInSession; }
    public void setWordsLearnedInSession(List<WordAnswer> wordsLearnedInSession) { this.wordsLearnedInSession = wordsLearnedInSession; }
    public int getTimeSpentSeconds() { return timeSpentSeconds; }
    public void setTimeSpentSeconds(int timeSpentSeconds) { this.timeSpentSeconds = timeSpentSeconds; }
    public Integer getScoreAchieved() { return scoreAchieved; }
    public void setScoreAchieved(Integer scoreAchieved) { this.scoreAchieved = scoreAchieved; }
}
