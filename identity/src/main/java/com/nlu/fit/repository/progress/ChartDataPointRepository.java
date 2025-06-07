package com.nlu.fit.repository.progress;

import com.nlu.fit.model.progress.ChartDataPoint;
import com.nlu.fit.model.progress.UserLearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ChartDataPointRepository extends JpaRepository<ChartDataPoint, Long> {
    Optional<ChartDataPoint> findByUserLearningPathAndDate(UserLearningPath userLearningPath, LocalDate date);
}
