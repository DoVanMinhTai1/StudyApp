package nlu.fit.studyappr.model;

public class ConfirmPathRequest {
    private String userId;
    private String pathId;

    public ConfirmPathRequest(String userId, String pathId) {
        this.userId = userId;
        this.pathId = pathId;
    }
}
