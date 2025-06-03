package nlu.fit.studyappr.model.grammar;

import java.io.Serializable;

public class GrammarExplanation implements Serializable {
    private Long id;

    private String structureImageUrl;

    private String usage;

    private String signalWords;

    private String verbRules;

    private String examples;

    private Long grammarLessonId;

    public GrammarExplanation(Long id, String structureImageUrl, String usage, String signalWords, String verbRules, String examples, Long grammarLessonId) {
        this.id = id;
        this.structureImageUrl = structureImageUrl;
        this.usage = usage;
        this.signalWords = signalWords;
        this.verbRules = verbRules;
        this.examples = examples;
        this.grammarLessonId = grammarLessonId;
    }

    public GrammarExplanation() {
    }

    @Override
    public String toString() {
        return "GrammarExplanation{" +
                "id=" + id +
                ", structureImageUrl='" + structureImageUrl + '\'' +
                ", usage='" + usage + '\'' +
                ", signalWords='" + signalWords + '\'' +
                ", verbRules='" + verbRules + '\'' +
                ", examples='" + examples + '\'' +
                ", grammarLessonId=" + grammarLessonId +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStructureImageUrl() {
        return structureImageUrl;
    }

    public void setStructureImageUrl(String structureImageUrl) {
        this.structureImageUrl = structureImageUrl;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getSignalWords() {
        return signalWords;
    }

    public void setSignalWords(String signalWords) {
        this.signalWords = signalWords;
    }

    public String getVerbRules() {
        return verbRules;
    }

    public void setVerbRules(String verbRules) {
        this.verbRules = verbRules;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public Long getGrammarLessonId() {
        return grammarLessonId;
    }

    public void setGrammarLessonId(Long grammarLessonId) {
        this.grammarLessonId = grammarLessonId;
    }
}

