package com.nlu.fit.repository.progress;

import com.nlu.fit.model.progress.LearningPathProposal;
import com.nlu.fit.model.progress.UserLearningPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLearningPathRepository extends JpaRepository<UserLearningPath, Long> {
    UserLearningPath findByUserIdAndIsActive(String userId, boolean active);
}
