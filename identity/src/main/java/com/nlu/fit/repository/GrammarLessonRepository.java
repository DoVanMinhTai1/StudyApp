package com.nlu.fit.repository;

import com.nlu.fit.model.GrammarExplanation;
import com.nlu.fit.model.GrammarLesson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GrammarLessonRepository extends JpaRepository<GrammarLesson, Long> {
}
