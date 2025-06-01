package nlu.fit.studyappr.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VocabularyWordViewModel implements Serializable {
    Long id;
    String word;
    String meaning;
    String levelTypeVocabulary;
    private String pronunciation; // You have text_pronunciation in XML, add this field
    private String exampleSentence; // You have text_example in XML, add this field
    String imageUrl;
    String audioUrl;
    private String quizQuestionPrompt; // e.g., "Nghĩa của từ X là gì?" or "Từ nào có nghĩa là Y?"
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctOption;

    public VocabularyWordViewModel() {
    }




    public String getQuizQuestionPrompt() {
        return quizQuestionPrompt;
    }

    public void setQuizQuestionPrompt(String quizQuestionPrompt) {
        this.quizQuestionPrompt = quizQuestionPrompt;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public VocabularyWordViewModel(Long id, String word, String meaning, String levelTypeVocabulary, String pronunciation, String exampleSentence, String imageUrl, String audioUrl, String quizQuestionPrompt, String optionA, String optionB, String optionC, String optionD, String correctOption) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.levelTypeVocabulary = levelTypeVocabulary;
        this.pronunciation = pronunciation;
        this.exampleSentence = exampleSentence;
        this.imageUrl = imageUrl;
        this.audioUrl = audioUrl;
        this.quizQuestionPrompt = quizQuestionPrompt;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    public void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
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
