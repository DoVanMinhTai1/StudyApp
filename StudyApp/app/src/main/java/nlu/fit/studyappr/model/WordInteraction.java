package nlu.fit.studyappr.model;

public class WordInteraction {
    String wordId;
    String status;

    public WordInteraction(String wordId, String status) {
        this.wordId = wordId;
        this.status = status;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
