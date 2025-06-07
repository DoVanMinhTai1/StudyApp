package com.nlu.fit.repository.progress;

import com.nlu.fit.model.progress.LearningPathProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LearningPathProposalRepository extends JpaRepository<LearningPathProposal, Long> {


    @Query("SELECT p FROM LearningPathProposal p WHERE " +
            "p.targetAchieved = :targetAchieved AND " +
            "p.durationForTarget = :durationForTarget AND " +
            "ABS(CAST(SUBSTRING(p.studyTimePerDay, 1, LOCATE(' ', p.studyTimePerDay) - 1) AS double) - :hoursPerDay) < 0.3")
    Optional<LearningPathProposal> findMatchingProposalApproximate(
            @Param("targetAchieved") String targetAchieved,
            @Param("durationForTarget") String durationForTarget,
            @Param("hoursPerDay") double hoursPerDay
    );

}
