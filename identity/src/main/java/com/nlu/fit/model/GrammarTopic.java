package com.nlu.fit.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @OneToMany(mappedBy = "grammar_topic")
    private List<GrammarLesson> grammarLessons;


}
