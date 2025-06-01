package nlu.fit.studyappr.model;

public class StartLearningRevewRequest {
    String method;
    String userId;

    public StartLearningRevewRequest(String method, String userId) {
        this.method = method;
        this.userId = userId;
    }

    public StartLearningRevewRequest() {
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
