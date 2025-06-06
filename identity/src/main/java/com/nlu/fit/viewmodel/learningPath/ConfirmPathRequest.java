package com.nlu.fit.viewmodel.learningPath;

public record ConfirmPathRequest(
        String userId,
        Long pathId
) {
}
