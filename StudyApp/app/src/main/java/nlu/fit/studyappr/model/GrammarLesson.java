package nlu.fit.studyappr.model;

import java.io.Serializable;
import java.util.List;

public class GrammarLesson implements Serializable {
    private Long id;

    private String examples;

    private String lessonType;

    private Long grammarTopicId;

    private GrammarExplanation grammarExplanations;

    private List<GrammarExcerciseQuestion> grammarExerciseQuestions;

    public GrammarLesson(Long id, String examples, String lessonType, Long grammarTopicId, GrammarExplanation grammarExplanations, List<GrammarExcerciseQuestion> grammarExerciseQuestions) {
        this.id = id;
        this.examples = examples;
        this.lessonType = lessonType;
        this.grammarTopicId = grammarTopicId;
        this.grammarExplanations = grammarExplanations;
        this.grammarExerciseQuestions = grammarExerciseQuestions;
    }

    public GrammarLesson() {
    }

    @Override
    public String toString() {
        return "GrammarLesson{" +
                "id=" + id +
                ", examples='" + examples + '\'' +
                ", lessonType='" + lessonType + '\'' +
                ", grammarTopicId=" + grammarTopicId +
                ", grammarExplanation=" + grammarExplanations +
                ", grammarExcerciseQuestionList=" + grammarExerciseQuestions +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public Long getGrammarTopicId() {
        return grammarTopicId;
    }

    public void setGrammarTopicId(Long grammarTopicId) {
        this.grammarTopicId = grammarTopicId;
    }

    public GrammarExplanation getGrammarExplanation() {
        return grammarExplanations;
    }

    public void setGrammarExplanation(GrammarExplanation grammarExplanation) {
        this.grammarExplanations = grammarExplanation;
    }

    public List<GrammarExcerciseQuestion> getGrammarExcerciseQuestionList() {
        return grammarExerciseQuestions;
    }

    public void setGrammarExcerciseQuestionList(List<GrammarExcerciseQuestion> grammarExcerciseQuestionList) {
        this.grammarExerciseQuestions = grammarExcerciseQuestionList;
    }
}
