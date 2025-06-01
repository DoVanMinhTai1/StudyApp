package nlu.fit.studyappr.model;

public class LearningPathRequest {
    private String targetScore;
    private String studyDuration;
    private int hoursPerWeek;

    public LearningPathRequest(String targetScore, String studyDuration, int hoursPerWeek) {
        this.targetScore = targetScore;
        this.studyDuration = studyDuration;
        this.hoursPerWeek = hoursPerWeek;
    }

    // Getters (and setters if needed by Gson/Moshi, though often not for request bodies)
    public String getTargetScore() { return targetScore; }
    public String getStudyDuration() { return studyDuration; }
    public int getHoursPerWeek() { return hoursPerWeek; }
}
