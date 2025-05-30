package com.nlu.fit.repository.vocabulary;

import com.nlu.fit.model.vocabulary.ReviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewScheduleRepository extends JpaRepository<ReviewSchedule, Long> {
}
