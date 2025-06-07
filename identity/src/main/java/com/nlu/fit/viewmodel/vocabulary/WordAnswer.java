package com.nlu.fit.viewmodel.vocabulary;

public record WordAnswer(
        Long wordId,
        Boolean status,
        String userAnswer
) {
}
