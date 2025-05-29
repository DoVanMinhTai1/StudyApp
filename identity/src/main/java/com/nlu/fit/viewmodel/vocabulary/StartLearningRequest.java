package com.nlu.fit.viewmodel.vocabulary;

import com.nlu.fit.model.vocabulary.VocabularyWord;

import java.util.List;

public record StartLearningRequest(
        Long topicId,
        String level,
        String method
) {
}
