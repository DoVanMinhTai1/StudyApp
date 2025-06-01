package com.nlu.fit.viewmodel.vocabulary;

import com.nlu.fit.enumeration.LearningStatusVocabulary;
import lombok.Builder;

@Builder
public record VocabularyWordsReviewList(
        Long id,
        Long sessionLearningColumnId,
        Long vocabularyWordId,
        LearningStatusVocabulary vocabularyStatus,
        String word,
        String meaning
)  {
}
