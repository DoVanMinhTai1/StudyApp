package com.nlu.fit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "grammar_review_result")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GrammarReviewResult {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int score;

    private LocalDateTime localDateTime;

    private String userId;

    @ManyToOne
    @JoinColumn(name = "grammar_topic_id")
    private GrammarTopic grammarTopic;
}
