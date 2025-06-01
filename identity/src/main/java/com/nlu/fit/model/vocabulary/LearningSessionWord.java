package com.nlu.fit.model.vocabulary;

import com.google.type.DateTime;
import com.nlu.fit.enumeration.LearningStatusVocabulary;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Table(name = "learning_session_word")
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class
LearningSessionWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private LearningStatusVocabulary status;

    private OffsetDateTime attemptAt;

    private String selectedOption;  // Đáp án người dùng chọn
    private boolean isCorrect;      // Kết quả đúng/sai


    @ManyToOne
    @JoinColumn(name = "learning_session_id")
    private LearningSession learningSession;

    @ManyToOne
    @JoinColumn(name = "vocabulary_word_id")
    private VocabularyWord vocabularyWord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_schedule_id") // Foreign key to review_schedule table
    private ReviewSchedule reviewSchedule; // This links it back


}
