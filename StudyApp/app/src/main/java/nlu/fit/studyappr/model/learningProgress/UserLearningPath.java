package nlu.fit.studyappr.model.learningProgress;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.util.List;

public class UserLearningPath {
    private Long id;
    private String userId;

    private LearningPathProposal learningPathProposal;

    @SerializedName("confirmed_at")
    private String confirmed_at;

    private ProgressStats progressStats;

    private List<ChartDataPoint> chartData;

    private boolean isActive;

    public UserLearningPath(Long id, String userId, LearningPathProposal learningPathProposal, String confirmed_at, ProgressStats progressStats, List<ChartDataPoint> chartData, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.learningPathProposal = learningPathProposal;
        this.confirmed_at = confirmed_at;
        this.progressStats = progressStats;
        this.chartData = chartData;
        this.isActive = isActive;
    }

    public UserLearningPath() {
    }

    @Override
    public String toString() {
        return "UserLearningPath{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", learningPathProposal=" + learningPathProposal +
                ", confirmed_at=" + confirmed_at +
                ", isActive=" + isActive +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LearningPathProposal getLearningPathProposal() {
        return learningPathProposal;
    }

    public void setLearningPathProposal(LearningPathProposal learningPathProposal) {
        this.learningPathProposal = learningPathProposal;
    }

    public String getConfirmed_at() {
        return confirmed_at;
    }

    public void setConfirmed_at(String confirmed_at) {
        this.confirmed_at = confirmed_at;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public ProgressStats getProgressStats() {
        return progressStats;
    }

    public void setProgressStats(ProgressStats progressStats) {
        this.progressStats = progressStats;
    }

    public List<ChartDataPoint> getChartData() {
        return chartData;
    }

    public void setChartData(List<ChartDataPoint> chartData) {
        this.chartData = chartData;
    }
}
