package com.nlu.fit.model.progress;

import com.nlu.fit.model.grammar.GrammarTopic;
import com.nlu.fit.model.vocabulary.VocabularyTopic;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "user_learning_path")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserLearningPath {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "learning_path_proposal_id")
    private LearningPathProposal learningPathProposal;

    private LocalDateTime confirmed_at;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progress_stats_id", referencedColumnName = "id")
    private ProgressStats progressStats;

    @OneToMany(mappedBy = "userLearningPath")
    private List<ChartDataPoint> chartData;

    private boolean isActive;

}
