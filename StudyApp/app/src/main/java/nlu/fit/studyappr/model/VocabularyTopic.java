package nlu.fit.studyappr.model;

public class VocabularyTopic {
    private Long id;
    private String title;

    public VocabularyTopic(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public VocabularyTopic() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title != null ? title : "No Title";
    }

}
