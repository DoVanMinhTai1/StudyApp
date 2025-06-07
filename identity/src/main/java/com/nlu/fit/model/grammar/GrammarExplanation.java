package com.nlu.fit.model.grammar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "grammar_explanation")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrammarExplanation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String structureImageUrl;

    @Column(name = "lessonUsage")
    private String usage;

    private String signalWords;

    private String verbRules;

    private String examples;

    @OneToOne
    @JoinColumn(name = "grammar_lesson_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private GrammarLesson grammarLesson;

}
