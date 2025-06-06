package com.nlu.fit.service.grammar;

import com.nlu.fit.enumeration.LessonType;
import com.nlu.fit.model.grammar.GrammarLesson;
import com.nlu.fit.repository.grammar.GrammarLessonRepository;
import com.nlu.fit.viewmodel.grammarexercise.GrammarLessonVm;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GrammarLessonService {
    private final GrammarLessonRepository grammarLessonRepository;

    public GrammarLessonVm getGrammarLessonById(Long id) {
        GrammarLesson grammarLesson = grammarLessonRepository.findById(id).orElse(null);
        return GrammarLessonVm.builder().id(grammarLesson.getId())
                .examples(grammarLesson.getExamples())
                .lessonType(grammarLesson.getLessonType())
                .grammarExplanations(grammarLesson.getGrammarExplanations())
                .grammarExerciseQuestions(grammarLesson.getGrammarExerciseQuestions())
                .build();
    }

    public List<GrammarLesson> getGrammarLessonByIdAndTypeExercise(Long id) {
        return grammarLessonRepository.findByIdAndLessonType(id, LessonType.EXERCISE);
    }

}
