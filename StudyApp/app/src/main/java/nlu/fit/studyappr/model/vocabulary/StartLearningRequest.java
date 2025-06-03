package nlu.fit.studyappr.model.vocabulary;

public class StartLearningRequest {
    Long topicId;
    String level;
    String method;

    public StartLearningRequest() {
    }

    public StartLearningRequest(Long topicId, String level, String method) {
        this.topicId = topicId;
        this.level = level;
        this.method = method;
    }

    @Override
    public String toString() {
        return "StartLearningRequest{" +
                "topicId=" + topicId +
                ", level='" + level + '\'' +
                ", method='" + method + '\'' +
                '}';
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
