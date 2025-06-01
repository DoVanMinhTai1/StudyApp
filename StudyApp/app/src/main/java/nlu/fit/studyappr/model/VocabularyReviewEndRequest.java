package nlu.fit.studyappr.model;

import java.io.Serializable;
import java.util.List;

public class VocabularyReviewEndRequest extends VocabularyEndRequest implements Serializable {

    private Long reviewScheduleId;
    private List<WordAnswer> answers; // This list will contain objects with wordId and known (boolean)

    private int wordsMarkedKnown;

    private int totalWordsInSession;


    public VocabularyReviewEndRequest(String userId, Long topicId, String topicType, int timeSpentSeconds, Long currentSessionId, Long reviewScheduleId, List<WordAnswer> answers, int wordsMarkedKnown, int totalWordsInSession) {
        super(userId, topicId, topicType, timeSpentSeconds, currentSessionId);
        this.reviewScheduleId = reviewScheduleId;
        this.answers = answers;
        this.wordsMarkedKnown = wordsMarkedKnown;
        this.totalWordsInSession = totalWordsInSession;
    }

    public Long getReviewScheduleId() {
        return reviewScheduleId;
    }

    public void setReviewScheduleId(Long reviewScheduleId) {
        this.reviewScheduleId = reviewScheduleId;
    }

    public int getWordsMarkedKnown() {
        return wordsMarkedKnown;
    }

    public void setWordsMarkedKnown(int wordsMarkedKnown) {
        this.wordsMarkedKnown = wordsMarkedKnown;
    }

    public int getTotalWordsInSession() {
        return totalWordsInSession;
    }

    public void setTotalWordsInSession(int totalWordsInSession) {
        this.totalWordsInSession = totalWordsInSession;
    }

    public List<WordAnswer> getAnswers() { return answers; }
    public void setAnswers(List<WordAnswer> answers) { this.answers = answers; }
    public VocabularyReviewEndRequest(String userId, Long topicId, String topicType, int timeSpentSeconds, Long currentSessionId) {
        super(userId, topicId, topicType, timeSpentSeconds, currentSessionId);
    }
}
