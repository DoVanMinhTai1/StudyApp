package com.nlu.fit.model.progress;

import com.nlu.fit.model.grammar.GrammarTopic;
import com.nlu.fit.model.vocabulary.VocabularyTopic;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "learning_path_proposal")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LearningPathProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String targetAchieved;

    private String durationForTarget;

    private int lessonsPerDay;

    private String studyTimePerDay;

    @ManyToMany
    @JoinTable(
            name = "learning_path_grammar_topics",
            joinColumns = @JoinColumn(name = "path_id"),
            inverseJoinColumns = @JoinColumn(name = "grammar_topic_id")
    )
    private List<GrammarTopic> grammarTopics;

    @ManyToMany
    @JoinTable(
            name = "learning_path_vocabulary_topics",
            joinColumns = @JoinColumn(name = "path_id"),
            inverseJoinColumns = @JoinColumn(name = "vocabulary_topic_id")
    )
    private List<VocabularyTopic> vocabularyTopics;



}
