package com.nlu.fit.model.vocabulary;

import com.google.type.DateTime;
import com.nlu.fit.enumeration.LevelTypeVocabulary;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "learning_session")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LearningSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long topicId;

    private LevelTypeVocabulary levelTypeVocabulary;

    private String method;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "vocabulary_topic_id")
    private VocabularyTopic vocabularyTopic;

    @OneToMany(mappedBy = "learningSession", cascade = CascadeType.ALL)
    private List<LearningSessionWord> learningSessionWord;

}
