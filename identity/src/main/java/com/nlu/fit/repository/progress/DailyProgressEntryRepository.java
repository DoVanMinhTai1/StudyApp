package com.nlu.fit.repository.progress;

import com.nlu.fit.model.progress.DailyProgressEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyProgressEntryRepository extends JpaRepository<DailyProgressEntry, Integer> {


    List<DailyProgressEntry> findByUserIdOrderByDate(String userId);
}
