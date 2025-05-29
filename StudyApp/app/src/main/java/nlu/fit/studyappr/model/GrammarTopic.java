package nlu.fit.studyappr.model;

import java.util.List;

public class GrammarTopic {
    private Long id;

    private String title;

    private int lesson_order;

    private GrammarLesson grammarLessons;

    public GrammarTopic(Long id, String title, int lesson_order, GrammarLesson grammarLessons) {
        this.id = id;
        this.title = title;
        this.lesson_order = lesson_order;
        this.grammarLessons = grammarLessons;
    }

    public GrammarTopic() {
    }

    @Override
    public String toString() {
        return "GrammarTopic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", lesson_order=" + lesson_order +
                ", grammarLesson=" + grammarLessons +
                '}';
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

    public int getLesson_order() {
        return lesson_order;
    }

    public void setLesson_order(int lesson_order) {
        this.lesson_order = lesson_order;
    }

    public GrammarLesson getGrammarLesson() {
        return grammarLessons;
    }

    public void setGrammarLesson(GrammarLesson grammarLessons) {
        this.grammarLessons = grammarLessons;
    }
}