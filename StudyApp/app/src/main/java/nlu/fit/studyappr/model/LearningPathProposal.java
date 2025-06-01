package nlu.fit.studyappr.model;

import java.util.List;

public class LearningPathProposal {
    private String targetAchieved;
    private String durationForTarget;
    private int lessonsPerDay;
    private String studyTimePerDay;
    private List<String> topics;
    private String pathId;

    public String getTargetAchieved() { return targetAchieved; }
    public String getDurationForTarget() { return durationForTarget; }
    public int getLessonsPerDay() { return lessonsPerDay; }
    public String getStudyTimePerDay() { return studyTimePerDay; }
    public List<String> getTopics() { return topics; }
    public String getPathId() { return pathId; }
}
