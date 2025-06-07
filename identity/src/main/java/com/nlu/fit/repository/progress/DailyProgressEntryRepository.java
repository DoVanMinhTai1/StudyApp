package com.nlu.fit.repository.progress;

import com.nlu.fit.model.progress.DailyProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyProgressEntryRepository extends JpaRepository<DailyProgressEntry, Integer> {


    List<DailyProgressEntry> findByUserIdOrderByDate(String userId);

    Optional<DailyProgressEntry> findByUserIdAndDateBetween(String userId, LocalDateTime dateAfter, LocalDateTime dateBefore);
}
