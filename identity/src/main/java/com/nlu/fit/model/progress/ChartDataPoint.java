package com.nlu.fit.model.progress;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "chart_data_point")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChartDataPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private int itemsCompletedToday;
    private long timeSpentSecondsToday;

    @ManyToOne
    @JoinColumn(name = "chartData")
    private UserLearningPath userLearningPath;
}
