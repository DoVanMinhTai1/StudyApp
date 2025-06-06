package nlu.fit.studyappr.model.learningProgress;

public class ProgressStats {
    private Long id;
    private int lessonsCompleted;

    private int learnedVocab;

    private int totalLessons;

    private double completionPercentage;

    public ProgressStats(Long id, int lessonsCompleted, int learnedVocab, int totalLessons, double completionPercentage) {
        this.id = id;
        this.lessonsCompleted = lessonsCompleted;
        this.learnedVocab = learnedVocab;
        this.totalLessons = totalLessons;
        this.completionPercentage = completionPercentage;
    }

    public ProgressStats() {
    }

    @Override
    public String toString() {
        return "ProgressStats{" +
                "id=" + id +
                ", lessonsCompleted=" + lessonsCompleted +
                ", totalLessons=" + totalLessons +
                ", completionPercentage=" + completionPercentage +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getLessonsCompleted() {
        return lessonsCompleted;
    }

    public void setLessonsCompleted(int lessonsCompleted) {
        this.lessonsCompleted = lessonsCompleted;
    }

    public int getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(int totalLessons) {
        this.totalLessons = totalLessons;
    }

    public double getCompletionPercentage() {
        return completionPercentage;
    }

    public void setCompletionPercentage(double completionPercentage) {
        this.completionPercentage = completionPercentage;
    }

    public int getLearnedVocab() {
        return learnedVocab;
    }

    public void setLearnedVocab(int learnedVocab) {
        this.learnedVocab = learnedVocab;
    }
}
