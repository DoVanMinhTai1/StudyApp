package nlu.fit.studyappr.model.learningProgress;

import java.io.Serializable;
import java.util.List;

import nlu.fit.studyappr.model.grammar.GrammarTopic;
import nlu.fit.studyappr.model.vocabulary.VocabularyTopic;

public class LearningPathProposal implements Serializable {
    private String id;

    private String targetAchieved;
    private String durationForTarget;
    private int lessonsPerDay;
    private String studyTimePerDay;

    private List<VocabularyTopic> vocabularyTopics;

    private List<GrammarTopic> grammarTopics;


    public LearningPathProposal(String id, String targetAchieved, String durationForTarget, int lessonsPerDay, String studyTimePerDay, List<VocabularyTopic> vocabularyTopics, List<GrammarTopic> grammarTopics) {
        this.id = id;
        this.targetAchieved = targetAchieved;
        this.durationForTarget = durationForTarget;
        this.lessonsPerDay = lessonsPerDay;
        this.studyTimePerDay = studyTimePerDay;
        this.vocabularyTopics = vocabularyTopics;
        this.grammarTopics = grammarTopics;
    }

    public LearningPathProposal() {
    }

    public List<VocabularyTopic> getVocabularyTopics() {
        return vocabularyTopics;
    }

    public void setVocabularyTopics(List<VocabularyTopic> vocabularyTopics) {
        this.vocabularyTopics = vocabularyTopics;
    }

    public List<GrammarTopic> getGrammarTopics() {
        return grammarTopics;
    }

    public void setGrammarTopics(List<GrammarTopic> grammarTopics) {
        this.grammarTopics = grammarTopics;
    }

    public String getTargetAchieved() { return targetAchieved; }
    public String getDurationForTarget() { return durationForTarget; }
    public int getLessonsPerDay() { return lessonsPerDay; }
    public String getStudyTimePerDay() { return studyTimePerDay; }

    public void setTargetAchieved(String targetAchieved) {
        this.targetAchieved = targetAchieved;
    }

    public void setDurationForTarget(String durationForTarget) {
        this.durationForTarget = durationForTarget;
    }

    public void setLessonsPerDay(int lessonsPerDay) {
        this.lessonsPerDay = lessonsPerDay;
    }

    public void setStudyTimePerDay(String studyTimePerDay) {
        this.studyTimePerDay = studyTimePerDay;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

