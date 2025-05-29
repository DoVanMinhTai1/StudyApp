package com.nlu.fit.model.vocabulary;

import com.google.type.DateTime;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "review_schedule")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @OneToMany(mappedBy = "reviewSchedule", cascade = CascadeType.ALL)
    private List<VocabularyWord> word_id;

    private LocalDateTime nextReviewTime;

    private String status;

    private LocalDateTime lastReviewAt;
}
