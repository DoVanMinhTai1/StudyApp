package nlu.fit.studyappr.model.vocabulary;

public class StartLearningReviewRequest {
    String method;
    String userId;

    public StartLearningReviewRequest(String method, String userId) {
        this.method = method;
        this.userId = userId;
    }

    public StartLearningReviewRequest() {
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
