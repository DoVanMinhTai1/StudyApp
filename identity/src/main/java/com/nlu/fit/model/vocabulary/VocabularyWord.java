package com.nlu.fit.model.vocabulary;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nlu.fit.enumeration.LevelTypeVocabulary;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "vocabulary_word")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VocabularyWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private LevelTypeVocabulary levelTypeVocabulary;

    private String word;

    private String meaning;

    private String pronunciation;

    private String imageUrl;

    private String audioUrl;

    @ManyToOne
    @JoinColumn(name = "vocabulary_topic_id")
    @JsonIgnore
    private VocabularyTopic vocabularyTopic;

    @OneToMany(mappedBy = "vocabularyWord", cascade = CascadeType.ALL)
    private List<LearningSessionWord> learningSessionWord;

    @ManyToOne
    @JoinColumn(name = "review_schedule_id")
    private ReviewSchedule reviewSchedule;
}
