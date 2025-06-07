package com.nlu.fit.repository.grammar;

import com.nlu.fit.enumeration.LessonType;
import com.nlu.fit.model.grammar.GrammarLesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarLessonRepository extends JpaRepository<GrammarLesson, Long> {
    List<GrammarLesson> findByIdAndLessonType(Long id, LessonType lessonType);

    @Query("SELECT g FROM GrammarLesson g where g.grammar_topic = :topicId")
    GrammarLesson findGrammarLessonByGrammar_topic(Long topicId);
}
