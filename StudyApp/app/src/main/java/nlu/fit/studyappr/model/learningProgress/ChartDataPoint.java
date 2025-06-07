package nlu.fit.studyappr.model.learningProgress;

import java.time.LocalDate;

public class ChartDataPoint {
    private Long id;


    private String date;
    private int itemsCompletedToday;
    private long timeSpentSecondsToday;

    public ChartDataPoint(Long id, String date, int itemsCompletedToday, long timeSpentSecondsToday) {
        this.id = id;
        this.date = date;
        this.itemsCompletedToday = itemsCompletedToday;
        this.timeSpentSecondsToday = timeSpentSecondsToday;
    }

    public ChartDataPoint() {
    }

    @Override
    public String toString() {
        return "ChartDataPoint{" +
                "id=" + id +
                ", date=" + date +
                ", itemsCompletedToday=" + itemsCompletedToday +
                ", timeSpentSecondsToday=" + timeSpentSecondsToday +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getItemsCompletedToday() {
        return itemsCompletedToday;
    }

    public void setItemsCompletedToday(int itemsCompletedToday) {
        this.itemsCompletedToday = itemsCompletedToday;
    }

    public long getTimeSpentSecondsToday() {
        return timeSpentSecondsToday;
    }

    public void setTimeSpentSecondsToday(long timeSpentSecondsToday) {
        this.timeSpentSecondsToday = timeSpentSecondsToday;
    }
}
