package nlu.fit.studyappr.model.learningProgress.configView;

public class DateHeaderItem implements DisplayableItem{
    private String date;

    public DateHeaderItem(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
