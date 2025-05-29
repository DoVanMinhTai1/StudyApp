package com.nlu.fit.viewmodel.vocabulary;

import com.nlu.fit.model.vocabulary.VocabularyWord;
import lombok.Builder;

import java.util.List;

@Builder
public record StartLearningResponse(
        Long sessionId,
        String method,
        List<VocabularyWordViewModel> words
) {
}
