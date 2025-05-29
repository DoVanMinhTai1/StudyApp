package com.nlu.fit.repository.vocabulary;

import com.nlu.fit.model.vocabulary.LearningSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningSessionRepository extends JpaRepository<LearningSession, Long> {
    List<LearningSession> findAllByUserId(String userId);

    List<LearningSession> findAllByUserIdAndMethod(String userId, String method);
}
