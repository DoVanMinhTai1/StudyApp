package nlu.fit.studyappr.model;

import java.util.List;

public class StartLearningReviewRequest {
    String method;
    String userId;

    public StartLearningReviewRequest(String method, String userId) {
        this.method = method;
        this.userId = userId;
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
