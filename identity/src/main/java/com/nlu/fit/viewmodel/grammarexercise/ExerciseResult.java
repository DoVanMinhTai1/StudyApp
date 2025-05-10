package com.nlu.fit.viewmodel.grammarexercise;

import java.util.List;

public record ExerciseResult(
        int correct,
        int total,
        List<InCorrectDetail> inCorrectDetailList
) {
}
