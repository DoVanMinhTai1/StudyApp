package nlu.fit.studyappr.model.vocabulary;

import java.io.Serializable;
import java.util.List;

public class StartLearningReviewResponse implements Serializable {
    Long reviewScheduleId;
    String method;
    List<VocabularyWordViewModel> words;

    public StartLearningReviewResponse(Long reviewScheduleId, String method, List<VocabularyWordViewModel> words) {
        this.reviewScheduleId = reviewScheduleId;
        this.method = method;
        this.words = words;
    }

    public StartLearningReviewResponse() {
    }

    public Long getReviewScheduleId() {
        return reviewScheduleId;
    }

    public void setReviewScheduleId(Long reviewScheduleId) {
        this.reviewScheduleId = reviewScheduleId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<VocabularyWordViewModel> getWords() {
        return words;
    }

    public void setWords(List<VocabularyWordViewModel> words) {
        this.words = words;
    }
}
