package com.nlu.fit.model.progress;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "daily_activity_item")
@Entity()
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DailyActivityItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int timeSpentSecondsToday;

    private String type;

}
