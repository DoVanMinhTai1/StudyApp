package com.nlu.fit.model.grammar;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String examples;

    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @OneToOne()
    @JoinColumn(name = "grammar_topic_id")
    @JsonIgnore
    private GrammarTopic grammar_topic;

    @OneToOne(mappedBy = "grammarLesson",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GrammarExplanation grammarExplanations;

    @OneToMany(mappedBy = "grammar_lesson",cascade = CascadeType.ALL)
    private List<GrammarExerciseQuestion> grammarExerciseQuestions;

}
