package nlu.fit.studyappr.model.learningProgress;

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

    public void setTargetAchieved(String targetAchieved) {
        this.targetAchieved = targetAchieved;
    }

    public void setDurationForTarget(String durationForTarget) {
        this.durationForTarget = durationForTarget;
    }

    public void setLessonsPerDay(int lessonsPerDay) {
        this.lessonsPerDay = lessonsPerDay;
    }

    public void setStudyTimePerDay(String studyTimePerDay) {
        this.studyTimePerDay = studyTimePerDay;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }
}

