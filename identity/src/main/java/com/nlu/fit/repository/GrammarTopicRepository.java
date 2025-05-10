package com.nlu.fit.repository;

import com.nlu.fit.model.GrammarExerciseQuestion;
import com.nlu.fit.model.GrammarTopic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarTopicRepository extends JpaRepository<GrammarTopic, Long> {
}
