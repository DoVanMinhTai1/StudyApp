package nlu.fit.studyappr.model.learningProgress;

public class ConfirmPathRequest {
    private String userId;
    private String pathId;

    public ConfirmPathRequest(String userId, String pathId) {
        this.userId = userId;
        this.pathId = pathId;
    }

    public ConfirmPathRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    @Override
    public String toString() {
        return "ConfirmPathRequest{" +
                "userId='" + userId + '\'' +
                ", pathId='" + pathId + '\'' +
                '}';
    }
}
