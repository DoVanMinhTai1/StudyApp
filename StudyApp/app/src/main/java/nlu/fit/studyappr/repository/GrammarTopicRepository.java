package nlu.fit.studyappr.repository;

import nlu.fit.studyappr.api.grammar.GrammarApiService;

public class GrammarTopicRepository {
    private final GrammarApiService grammarApiService;

    public GrammarTopicRepository(GrammarApiService grammarApiService) {
        this.grammarApiService = grammarApiService;
    }


}
