package nlu.fit.studyappr.model.learningProgress; // Your package

import java.io.Serializable;
import java.util.List;

public class DailyProgressEntry implements Serializable {
    private String date; // e.g., "2023-10-27" or "Hôm nay (27/10/2023)"
    private List<DailyActivityItem> activities;

    // Constructors, Getters, Setters
    public DailyProgressEntry() {}

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<DailyActivityItem> getActivities() { return activities; }
    public void setActivities(List<DailyActivityItem> activities) { this.activities = activities; }
}