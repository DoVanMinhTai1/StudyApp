package nlu.fit.studyappr.model;

public class VocabularyWordsReviewList {
    Long id;
    Long sessionLearningColumnId;
    Long vocabularyWordId;
    LearningStatusVocabulary vocabularyStatus;
    String word;
    String mean;

    public VocabularyWordsReviewList(Long sessionLearningColumnId, Long vocabularyWordId, LearningStatusVocabulary vocabularyStatus, String word, String mean) {
        this.sessionLearningColumnId = sessionLearningColumnId;
        this.vocabularyWordId = vocabularyWordId;
        this.vocabularyStatus = vocabularyStatus;
        this.word = word;
        this.mean = mean;
    }


    public Long getSessionLearningColumnId() {
        return sessionLearningColumnId;
    }

    public void setSessionLearningColumnId(Long sessionLearningColumnId) {
        this.sessionLearningColumnId = sessionLearningColumnId;
    }

    public Long getVocabularyWordId() {
        return vocabularyWordId;
    }

    public void setVocabularyWordId(Long vocabularyWordId) {
        this.vocabularyWordId = vocabularyWordId;
    }

    public LearningStatusVocabulary getVocabularyStatus() {
        return vocabularyStatus;
    }

    public void setVocabularyStatus(LearningStatusVocabulary vocabularyStatus) {
        this.vocabularyStatus = vocabularyStatus;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }
}
