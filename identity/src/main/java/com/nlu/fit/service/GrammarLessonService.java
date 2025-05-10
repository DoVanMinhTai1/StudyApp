package com.nlu.fit.service;

import com.nlu.fit.model.GrammarLesson;
import com.nlu.fit.repository.GrammarLessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GrammarLessonService {
    private final GrammarLessonRepository grammarLessonRepository;

    public GrammarLesson getGrammarLessonById(Long id) {
        return grammarLessonRepository.findById(id).orElse(null);
    }
}
