package com.nlu.fit.repository.grammar;

import com.nlu.fit.model.grammar.GrammarExerciseQuestion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GrammarExerciseQuestionRepository extends JpaRepository<GrammarExerciseQuestion, Long> {

    @Query("select q from GrammarExerciseQuestion  q where q.grammar_lesson.id = :lessonId")
    List<GrammarExerciseQuestion> findAllByGrammarlesson(Long lesssonId);
}
