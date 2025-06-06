package com.nlu.fit.model.progress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "progress_stats")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProgressStats {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int lessonsCompleted;

    private int learnedVocab;

    private int totalLessons;

    private double completionPercentage;

    @OneToOne
    @JsonIgnore
    private UserLearningPath userLearningPath;
}
