package com.nlu.fit.viewmodel.learningPath;

import com.nlu.fit.model.grammar.GrammarTopic;
import com.nlu.fit.model.vocabulary.VocabularyTopic;
import lombok.Builder;

import java.util.List;

@Builder
public record LearningPathProposalResponse(
        Long id,
        String targetAchieved,
        String durationForTarget,
        int lessonsPerDay,
        String studyTimePerDay,
        List<VocabularyTopic> vocabularyTopics,
        List<GrammarTopic> grammarTopics,
        String pathId

) {
}
