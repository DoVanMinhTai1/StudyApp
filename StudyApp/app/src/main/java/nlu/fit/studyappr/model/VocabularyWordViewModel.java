package nlu.fit.studyappr.model;

import java.io.Serializable;

public class VocabularyWordViewModel implements Serializable {
    Long id;
    String word;
    String meaning;
    String levelTypeVocabulary;
    String imageUrl;
    String audioUrl;

    public VocabularyWordViewModel() {
    }

    public VocabularyWordViewModel(Long id, String word, String meaning, String levelTypeVocabulary, String imageUrl, String audioUrl) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.levelTypeVocabulary = levelTypeVocabulary;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getLevelTypeVocabulary() {
        return levelTypeVocabulary;
    }

    public void setLevelTypeVocabulary(String levelTypeVocabulary) {
        this.levelTypeVocabulary = levelTypeVocabulary;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    @Override
    public String toString() {
        return "VocabularyWordViewModel{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                ", levelTypeVocabulary='" + levelTypeVocabulary + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", audioUrl='" + audioUrl + '\'' +
                '}';
    }
}
