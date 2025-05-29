package com.nlu.fit.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "user_answer")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswer {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double score;

    @ManyToOne
    @JoinColumn(name = "grammar_exercise_question_id")
    private GrammarExerciseQuestion grammarExerciseQuestion;

    @ManyToOne
    @JoinColumn(name = "grammar_review_result_id")
    private GrammarReviewResult grammarReviewResult;
}
