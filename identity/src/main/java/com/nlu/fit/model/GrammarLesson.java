package com.nlu.fit.model;

import com.nlu.fit.enumeration.LessonType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Table(name = "grammar_lesson")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrammarLesson {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examples;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "grammar_topic_id")
    private GrammarTopic grammar_topic;

    @OneToOne(optional = true)
    @JoinColumn(name = "grammar_explanations_id")
    private GrammarExplanation grammarExplanations;

    @OneToMany(mappedBy = "grammar_lesson")
    private List<GrammarExerciseQuestion> grammarExerciseQuestions;

}
