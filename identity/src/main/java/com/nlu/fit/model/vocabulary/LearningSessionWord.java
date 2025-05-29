package com.nlu.fit.model.vocabulary;

import com.google.type.DateTime;
import com.nlu.fit.enumeration.LearningStatusVocabulary;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "learning_session_word")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class
LearningSessionWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private LearningStatusVocabulary status;

    private LocalDateTime attemptAt;

    @ManyToOne
    @JoinColumn(name = "learning_session_id")
    private LearningSession learningSession;

    @ManyToOne
    @JoinColumn(name = "vocabulary_word_id")
    private VocabularyWord vocabularyWord;

}
