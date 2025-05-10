package com.nlu.fit.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "grammar_lesson")
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
    private GrammarLesson grammarLesson;

}
