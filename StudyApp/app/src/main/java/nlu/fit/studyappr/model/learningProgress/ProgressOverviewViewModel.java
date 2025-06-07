package nlu.fit.studyappr.model.learningProgress;

public class ProgressOverviewViewModel {
    int grammarProgress;
    int vocabularyProgress;
    int learningPathProgress;

    public ProgressOverviewViewModel(int grammarProgress, int vocabularyProgress, int learningPathProgress) {
        this.grammarProgress = grammarProgress;
        this.vocabularyProgress = vocabularyProgress;
        this.learningPathProgress = learningPathProgress;
    }

    public ProgressOverviewViewModel() {
    }

    public int getGrammarProgress() {
        return grammarProgress;
    }

    public void setGrammarProgress(int grammarProgress) {
        this.grammarProgress = grammarProgress;
    }

    public int getVocabularyProgress() {
        return vocabularyProgress;
    }

    public void setVocabularyProgress(int vocabularyProgress) {
        this.vocabularyProgress = vocabularyProgress;
    }

    public int getLearningPathProgress() {
        return learningPathProgress;
    }

    public void setLearningPathProgress(int learningPathProgress) {
        this.learningPathProgress = learningPathProgress;
    }
}
