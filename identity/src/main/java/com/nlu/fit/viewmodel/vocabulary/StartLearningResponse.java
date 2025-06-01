package com.nlu.fit.viewmodel.vocabulary;

import com.nlu.fit.model.vocabulary.VocabularyWord;
import lombok.Builder;

import java.util.List;

@Builder
public record StartLearningResponse(
        Long sessionId,
        String method,
        Long topicId,
        String title,
        List<VocabularyWordViewModel> words
) {
}
