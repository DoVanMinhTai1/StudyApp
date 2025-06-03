package com.nlu.fit.model.grammar;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "grammar_topic")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrammarTopic {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int lesson_order;

    @OneToOne(mappedBy = "grammar_topic",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private GrammarLesson grammarLessons;


}
