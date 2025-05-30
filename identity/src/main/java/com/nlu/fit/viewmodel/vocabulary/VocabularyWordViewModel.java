package com.nlu.fit.viewmodel.vocabulary;

import com.nlu.fit.enumeration.LearningStatusVocabulary;
import lombok.Builder;

@Builder
public record VocabularyWordViewModel(
        Long sessionLearningId,
        LearningStatusVocabulary vocabularyStatus,
        Long id,
        String word,
        String meaning,
        String levelTypeVocabulary,
        String imageUrl,
        String audioUrl
) {
}
