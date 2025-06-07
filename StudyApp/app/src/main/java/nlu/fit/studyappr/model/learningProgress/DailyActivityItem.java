package nlu.fit.studyappr.model.learningProgress;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DailyActivityItem implements Serializable {
    private String activityId;
    private String title;

    private int timeSpentSecondsToday;

    private String type;

    // Constructors, Getters, Setters
    public DailyActivityItem() {}

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getTimeSpentSecondsToday() {
        return timeSpentSecondsToday;
    }

    public void setTimeSpentSecondsToday(int timeSpentSecondsToday) {
        this.timeSpentSecondsToday = timeSpentSecondsToday;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
