package com.nlu.fit.service.progress;

import com.nlu.fit.repository.progress.LearningPathProposalRepository;
import com.nlu.fit.viewmodel.learningPath.LearningPathProposalResponse;
import com.nlu.fit.viewmodel.learningPath.LearningPathRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProgressService {
    private final LearningPathProposalRepository learningPathProposalRepository;

    public LearningPathProposalResponse getLearningPathProposal(LearningPathRequest request) {
        return null;
    }
}
