package nlu.fit.studyappr.model;

import java.io.Serializable;

public class DailyActivityItem implements Serializable {
    private String activityId;
    private String title;
    private String details;
    private String type; // Optional

    // Constructors, Getters, Setters
    public DailyActivityItem() {}

    public String getActivityId() { return activityId; }
    public void setActivityId(String activityId) { this.activityId = activityId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
