package com.nlu.fit.repository.vocabulary;

import com.nlu.fit.model.vocabulary.LearningSessionWord;
import com.nlu.fit.model.vocabulary.ReviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface LearningSessionWordRepository extends JpaRepository<LearningSessionWord, Long> {


    Optional<LearningSessionWord> findByLearningSession_IdAndVocabularyWord_Id(Long learningSessionId, Long vocabularyWordId);


    List<LearningSessionWord> findAllByUserId(String userId);

    List<LearningSessionWord> findAllByLearningSession_IdIn(List<Long> learningSessionId);

    LearningSessionWord findByReviewScheduleAndVocabularyWord_Id(ReviewSchedule reviewSchedule, Long vocabularyWordId);
}
