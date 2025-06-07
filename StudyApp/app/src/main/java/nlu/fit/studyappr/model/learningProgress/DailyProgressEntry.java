package nlu.fit.studyappr.model.learningProgress; // Your package

import java.io.Serializable;
import java.util.List;

import nlu.fit.studyappr.model.learningProgress.configView.DisplayableItem;

public class DailyProgressEntry implements Serializable, DisplayableItem {
    private String date;
    private List<DailyActivityItem> activities;

    private String userId;

    public DailyProgressEntry(String date, List<DailyActivityItem> activities, String userId) {
        this.date = date;
        this.activities = activities;
        this.userId = userId;
    }

    public DailyProgressEntry() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public List<DailyActivityItem> getActivities() { return activities; }
    public void setActivities(List<DailyActivityItem> activities) { this.activities = activities; }
}