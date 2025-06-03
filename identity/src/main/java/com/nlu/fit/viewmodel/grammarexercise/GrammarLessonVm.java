package com.nlu.fit.viewmodel.grammarexercise;

import com.nlu.fit.enumeration.LessonType;
import com.nlu.fit.model.grammar.GrammarExerciseQuestion;
import com.nlu.fit.model.grammar.GrammarExplanation;
import lombok.Builder;

import java.util.List;

@Builder
public record GrammarLessonVm(
        Long id,
        String examples,
        LessonType lessonType,
        GrammarExplanation grammarExplanations,
        List<GrammarExerciseQuestion> grammarExerciseQuestions
) {
}
